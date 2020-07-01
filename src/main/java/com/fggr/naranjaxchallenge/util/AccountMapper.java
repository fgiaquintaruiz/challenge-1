package com.fggr.naranjaxchallenge.util;

import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dao.TransactionDao;
import com.fggr.naranjaxchallenge.dto.AccountDto;
import com.fggr.naranjaxchallenge.dto.TransactionDto;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AccountMapper {

    public static synchronized AccountDto mapDto(AccountDao accountDao) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        AccountDto accountDto = new AccountDto();
        Class<?> objClass = accountDao.getClass();
        for (Field field : objClass.getDeclaredFields()) {

            Object objField = new PropertyDescriptor(field.getName(),
                        AccountDao.class).getReadMethod().invoke(accountDao);

            if("transactions".equals(field.getName())){
                List<TransactionDao> transactionsDao = (List<TransactionDao>) objField;
                List<TransactionDto> transactionsDto = new ArrayList<>();
                for(TransactionDao transactionDao : transactionsDao){
                    transactionsDto.add(TransactionMapper.mapDto(transactionDao));
                }

                new PropertyDescriptor(field.getName(), AccountDto.class)
                        .getWriteMethod().invoke(accountDto, transactionsDto);
            } else {

                new PropertyDescriptor(field.getName(), AccountDto.class)
                        .getWriteMethod().invoke(accountDto, objField);
            }

        }
        return accountDto;
    }

    public static synchronized AccountDao mapDao(AccountDto accountDto) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        AccountDao accountDao = new AccountDao();
        Class<?> objClass = accountDto.getClass();
        for (Field field : objClass.getDeclaredFields()) {

            Object objField = new PropertyDescriptor(field.getName(),
                        AccountDto.class).getReadMethod().invoke(accountDto);

                if("transactions".equals(field.getName())){
                    List<TransactionDto> transactionsDto = (List<TransactionDto>) objField;
                    List<TransactionDao> transactionsDao = new ArrayList<>();
                    for(TransactionDto transactionDto : transactionsDto){
                        transactionsDao.add(TransactionMapper.mapDao(transactionDto));
                    }
                    new PropertyDescriptor(field.getName(), AccountDao.class)
                            .getWriteMethod().invoke(accountDao, transactionsDao);
                } else {
                    new PropertyDescriptor(field.getName(), AccountDao.class)
                            .getWriteMethod().invoke(accountDao, objField);
                }

        }
        return accountDao;
    }

}
