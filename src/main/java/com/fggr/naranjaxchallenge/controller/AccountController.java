package com.fggr.naranjaxchallenge.controller;

import com.fggr.naranjaxchallenge.dto.AccountDto;
import com.fggr.naranjaxchallenge.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/account")
    public @ResponseBody Object createAccount(@RequestBody AccountDto accountDto) throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        return accountService.validateAccount(accountDto);

    }

}
