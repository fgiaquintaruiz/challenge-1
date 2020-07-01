package com.fggr.naranjaxchallenge.controller;

import com.fggr.naranjaxchallenge.dto.TransactionDto;
import com.fggr.naranjaxchallenge.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/account/{id}/transaction")
    public @ResponseBody Object executeTransaction(@RequestBody TransactionDto transactionDto) throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        return transactionService.executeTransaction(transactionDto);

    }

}
