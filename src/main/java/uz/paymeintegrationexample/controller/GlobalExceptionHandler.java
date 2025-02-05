package uz.paymeintegrationexample.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.paymeintegrationexample.domain.payme.responses.PaymeError;
import uz.paymeintegrationexample.domain.payme.responses.PaymeErrorResponse;
import uz.paymeintegrationexample.domain.payme.exceptions.PaymeException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PaymeException.class)
    public PaymeErrorResponse handlePaymeException(PaymeException e){
        return new PaymeErrorResponse(new PaymeError(e.getError(), e.getData()));
    }
}
