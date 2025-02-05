package uz.paymeintegrationexample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.paymeintegrationexample.domain.enums.OrderStatus;
import uz.paymeintegrationexample.domain.payme.requests.PaymeParam;
import uz.paymeintegrationexample.domain.payme.requests.PaymeRequest;
import uz.paymeintegrationexample.domain.entity.Order;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;
import uz.paymeintegrationexample.domain.payme.enums.PaymeError;
import uz.paymeintegrationexample.domain.payme.enums.PaymeTransactionState;
import uz.paymeintegrationexample.domain.payme.exceptions.PaymeException;
import uz.paymeintegrationexample.domain.payme.responses.*;
import uz.paymeintegrationexample.domain.payme.util.PaymeUtil;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymeService {
    private static final Long time_expired = 43_200_000L;
    private final OrderService orderService;
    private final TransactionService transactionService;

    public PaymeSuccessResponse pay(PaymeRequest request) {
        return new PaymeSuccessResponse(switch (request.getMethod()) {
            case "CheckPerformTransaction" -> checkPerformTransaction(request.getParams());
            case "CreateTransaction" -> createTransaction(request.getParams());
            case "PerformTransaction" -> performTransaction(request.getParams());
            case "CancelTransaction" -> cancelTransaction(request.getParams());
            case "CheckTransaction" -> checkTransaction(request.getParams());
            case "GetStatement" -> getStatement(request.getParams());
            default -> throw new PaymeException(PaymeError.INVALID_METHOD);
        });
    }

    /**
     * GetStatement - bu ma'lum vaqt oralig'ida tranzaksiyalarni olish
     */
    private GetStatementResult getStatement(PaymeParam params) {
        // from va to larni qiyamatini birga kamaytiramiz va ko'paytiramiz
        // chunki between'da ikkala sonlarni o'rtasidagilar olinadi lekin biz teng bo'lganlarini ham qaytarishimiz kerak
        return new GetStatementResult(PaymeUtil.mapToGetTransactionDto(
                transactionService.findAllByCreateTimeIsBetween(params.getFrom() - 1, params.getTo() + 1))
        );
    }

    /**
     * CheckTransaction - tranzaksiyani tekshirish
     */
    private CheckTransactionResult checkTransaction(PaymeParam params) {
        PaymeTransaction transaction = getTransactionOrThrowExc(params.getId());

        return CheckTransactionResult.from(transaction);
    }

    /**
     * CancelTransaction - tranzaksiyani bekor qilish
     */
    private CancelTransactionResult cancelTransaction(PaymeParam params) {
        PaymeTransaction paymeTransaction = getTransactionOrThrowExc(params.getId());

        boolean canceled = false;

        if (Objects.equals(paymeTransaction.getState(), PaymeTransactionState.PENDING)) {
            // sizning logika bo'yicha tranzaksiyani bekor qilish
            paymeTransaction.setState(PaymeTransactionState.PENDING_CANCELED);
            canceled = true;
        } else if(Objects.equals(paymeTransaction.getState(), PaymeTransactionState.PAID)){
            // Agar tranzaksiya holati PAID bo'lmasa bu tranzaksiyani shunchaki "Paid canceled" qilib qo'yamiz
            paymeTransaction.setState(PaymeTransactionState.PAID_CANCELED);
            canceled = true;
        }

        if (canceled) {
            paymeTransaction.setCancelTime(System.currentTimeMillis());
            paymeTransaction.setReason(params.getReason());
        }

        transactionService.save(paymeTransaction);

        // tranzaksiya bekor qilinayotganidan keyin buyurtma holatini bekor qilish
        Order order = getOrderOrThrowExc(paymeTransaction.getOrderId());
        order.setStatus(OrderStatus.CANCELED);
        orderService.save(order);

        // tranzaksiya bekor qilindi va uni ma'lumotlari qaytariladi
        return CancelTransactionResult.from(paymeTransaction);
    }

    /**
     * PerformTransaction - tranzaksiyani amalga oshirish
     * bu CreateTransaction dan keyin bo'ladi va to‘lov tasdiqlanadi, mablag‘ merchant hisobiga o‘tkaziladi va buyurtma holati "PAID" deb belgilanadi.
     */
    private PerformTransactionResult performTransaction(PaymeParam params) {
        PaymeTransaction transaction = getTransactionOrThrowExc(params.getId());

        if (!Objects.equals(transaction.getState(), PaymeTransactionState.PENDING)) {
            if (!Objects.equals(transaction.getState(), PaymeTransactionState.PAID)) {
                throw new PaymeException(PaymeError.CANT_DO_OPERATION);
            }

            // Agar tranzaksiya holati PAID bo'lsa bu tranzaksiyani shunchaki qaytaramiz
            return PerformTransactionResult.from(transaction);
        }

        checkTimeoutOrThrowExc(transaction);

        transaction.setState(PaymeTransactionState.PAID);
        transaction.setPerformTime(System.currentTimeMillis());
        transactionService.save(transaction);

        // bu yerda sizning logikangiz bo'ladi tranzaksiyani amalga oshirilgandan so'ng
        Order order = getOrderOrThrowExc(transaction.getOrderId());
        order.setStatus(OrderStatus.COMPLETED);
        orderService.save(order);

        return PerformTransactionResult.from(transaction);
    }

    /**
     * CreateTransaction - tranzaksiya yaratish bu CheckPerformTransactiondan keyin keladi
     */
    private CreateTransactionResult createTransaction(PaymeParam params) {
        PaymeTransaction transaction = transactionService.findById(params.getId());

        if (Objects.nonNull(transaction)) {
            ifTransactionExist(transaction);
        } else {
            // agar tranzaksiya mavjud emas bo'lsa bu holatda tranzaksiya summasi va shunaqa ma'lumotlarni tekshirish uchun
            // CheckPerformTransaction ni chaqiramiz
            checkPerformTransaction(params);

            // agar order uchun oldin tranzaksiya yaratilgan bo'lsa bu tranzaksiyani yaratishni to'xtatamiz
            if (transactionService.existsByOrderId(params.getAccount().getOrderId())) {
                throw new PaymeException(PaymeError.ORDER_NOT_FOUND);
            }

            transaction = createNewTransaction(params);
        }

        return CreateTransactionResult.from(transaction);
    }

    /**
     * CheckPerformTransaction
     * tranzaksiyani yaratishdan oldin tekshirish
     * ya'ni bizda shu tranzaksiya uchun order borligini va tranzaksiya summasi to'g'ri kelishini tekshirish
     */
    private CheckPerformTransactionResult checkPerformTransaction(PaymeParam params) {
        Order order = getOrderOrThrowExc(params.getAccount().getOrderId());

        // Agar tranzaksiya summasi to'g'ri kelmasa exception otamiz
        checkAmountOrThrowExc(order.getProduct().getPrice(), params.getAmount());

        if (!Objects.equals(order.getStatus(), OrderStatus.NEW)) {
            throw new PaymeException(PaymeError.ORDER_NOT_FOUND, "order_id");
        }

        // hammaga to'g'ri kelgani uchun true qaytaradi
        return new CheckPerformTransactionResult(true);
    }

    private PaymeTransaction getTransactionOrThrowExc(String id) {
        PaymeTransaction transaction = transactionService.findById(id);

        if (Objects.isNull(transaction)) {
            throw new PaymeException(PaymeError.TRANSACTION_NOT_FOUND);
        }

        return transaction;
    }

    private PaymeTransaction createNewTransaction(PaymeParam params) {
        return transactionService.save(
                PaymeTransaction.from(params)
        );
    }

    private void ifTransactionExist(PaymeTransaction transaction) {
        // agar tranzaksiya holati pending bo'lmasa exception otamiz
        if (transaction.getState() != PaymeTransactionState.PENDING) {
            throw new PaymeException(PaymeError.CANT_DO_OPERATION);
        }

        // agar timeout bo'lsa true qaytaradi
        checkTimeoutOrThrowExc(transaction);
    }

    private void checkTimeoutOrThrowExc(PaymeTransaction transaction) {
        if (System.currentTimeMillis() - transaction.getCreateTime() > time_expired) {
            cancelTransactionDueToTimeout(transaction);

            throw new PaymeException(PaymeError.CANT_DO_OPERATION);
        }
    }

    private void checkAmountOrThrowExc(Long price, Long amount) {
        if (!Objects.equals(price, PaymeUtil.normalizeAmount(amount))) {
            throw new PaymeException(PaymeError.INVALID_AMOUNT);
        }
    }

    private Order getOrderOrThrowExc(Long orderId) {
        Order order = orderService.findById(orderId);

        if (Objects.isNull(order)) {
            throw new PaymeException(PaymeError.ORDER_NOT_FOUND, "order_id");
        }

        return order;
    }

    private void cancelTransactionDueToTimeout(PaymeTransaction transaction) {
        transaction.setState(PaymeTransactionState.PENDING_CANCELED);
        transaction.setReason(4);
        transaction.setCancelTime(System.currentTimeMillis());
        transactionService.save(transaction);

        // bu yerda sizning logikangiz bo'ladi timeout bo'lgan tranzaksiyani bekor qilish uchun
        Order order = getOrderOrThrowExc(transaction.getOrderId());
        order.setStatus(OrderStatus.CANCELED);
        orderService.save(order);
    }
}

