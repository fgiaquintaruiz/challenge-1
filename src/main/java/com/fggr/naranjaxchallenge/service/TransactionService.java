package com.fggr.naranjaxchallenge.service;

import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dao.TransactionDao;
import com.fggr.naranjaxchallenge.dto.AccountDto;
import com.fggr.naranjaxchallenge.dto.ErrorResponse;
import com.fggr.naranjaxchallenge.dto.TransactionDto;
import com.fggr.naranjaxchallenge.enumators.TypeEnum;
import com.fggr.naranjaxchallenge.repository.AccountRepository;
import com.fggr.naranjaxchallenge.repository.TransactionRepository;
import com.fggr.naranjaxchallenge.util.AccountMapper;
import com.fggr.naranjaxchallenge.util.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ValidationService validationService;

    public Object executeTransaction(TransactionDto transactionDto) throws IllegalAccessException, IntrospectionException, InvocationTargetException {

        TransactionDao transactionDao = TransactionMapper.mapDao(transactionDto);
        List<AccountDao> accountDaoList;
            accountDaoList = accountRepository.findByIdEquals(transactionDto.getAccountId());

        if (accountDaoList.size() == 1) {
            AccountDao accountDao = accountDaoList.get(0);

            ErrorResponse errorResponse = validationService.validateOperation(accountDao, transactionDao);
            if(errorResponse.getViolations().size() > 0) {
                return errorResponse;
            } else {

                accountDao.getTransactions().add(transactionDao);
                if(TypeEnum.DEPOSIT.equals(transactionDao.getType())){
                    accountDao.setAvailableAmount(accountDao.getAvailableAmount().add(transactionDao.getAmount()));
                } else {
                    accountDao.setAvailableAmount(accountDao.getAvailableAmount().subtract(transactionDao.getAmount()));
                }

                synchronized (accountDao){
                    accountRepository.save(accountDao);
                }


                return AccountMapper.mapDto(accountDao);
            }
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(500);
            errorResponse.setMessage("account id not found");
            return errorResponse;
        }

    }

}
