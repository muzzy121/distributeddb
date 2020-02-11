package com.muzzy.clientaccess.controller;

import com.muzzy.dto.TransactionDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TransactionValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TransactionDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "value", "field.value.empty");
        ValidationUtils.rejectIfEmpty(errors, "receiver", "field.value.empty");
        TransactionDto transactionDto = (TransactionDto) o;
        //two ways to add reject information
        if (transactionDto.getReceiver().equals("#")) {
//            errors.rejectValue("receiver", "field.value.empty");
        }
    }
}
