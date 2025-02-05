package uz.paymeintegrationexample.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.paymeintegrationexample.domain.payme.requests.PaymeRequest;
import uz.paymeintegrationexample.domain.payme.responses.PaymeSuccessResponse;
import uz.paymeintegrationexample.domain.payme.util.PaymeUtil;
import uz.paymeintegrationexample.service.PaymeService;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final PaymeService paymeService;

    @PostMapping("/payme")
    public PaymeSuccessResponse paymeTransaction(@RequestBody PaymeRequest request, HttpServletRequest httpServletRequest){
        PaymeUtil.authorizeToken(httpServletRequest.getHeader("Authorization"));

        return paymeService.pay(request);
    }
}
