package com.fggr.naranjaxchallenge.service;

import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dto.AccountDto;
import com.fggr.naranjaxchallenge.dto.ErrorResponse;
import com.fggr.naranjaxchallenge.repository.AccountRepository;
import com.fggr.naranjaxchallenge.util.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ValidationService validationService;

    public Object validateAccount(AccountDto accountDto) throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        AccountDao accountDao = AccountMapper.mapDao(accountDto);
        ErrorResponse errorResponse = validationService.validateOperation(accountDao,null);

        if(errorResponse.getViolations().size() > 0){
            return errorResponse;

        } else {
            AccountDao accountDaoSaved;
            synchronized (accountDao){
                //insercion
                accountDaoSaved = accountRepository.save(accountDao);
            }

            return AccountMapper.mapDto(accountDaoSaved);
        }
    }
}
