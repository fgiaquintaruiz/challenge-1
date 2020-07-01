package com.fggr.naranjaxchallenge.util;

import com.fggr.naranjaxchallenge.dao.TransactionDao;
import com.fggr.naranjaxchallenge.dto.TransactionDto;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class TransactionMapper {
    
    public static synchronized TransactionDto mapDto(TransactionDao TransactionDao) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        TransactionDto TransactionDto = new TransactionDto();
        Class<?> objClass = TransactionDao.getClass();
        for (Field field : objClass.getDeclaredFields()) {

                if(!"transactionId".equals(field.getName())) {
                    Object objField = new PropertyDescriptor(field.getName(),
                            TransactionDao.class).getReadMethod().invoke(TransactionDao);

                    // Invoke the setter method on the Institution2 object.
                    new PropertyDescriptor(field.getName(), TransactionDto.class)
                            .getWriteMethod().invoke(TransactionDto, objField);
                }

        }
        return TransactionDto;
    }

    public static synchronized TransactionDao mapDao(TransactionDto TransactionDto) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        TransactionDao TransactionDao = new TransactionDao();
        Class<?> objClass = TransactionDto.getClass();
        for (Field field : objClass.getDeclaredFields()) {

            if(!"transactionId".equals(field.getName())) {
                Object objField = new PropertyDescriptor(field.getName(),
                        TransactionDto.class).getReadMethod().invoke(TransactionDto);

                new PropertyDescriptor(field.getName(), TransactionDao.class)
                        .getWriteMethod().invoke(TransactionDao, objField);
            }

        }
        return TransactionDao;
    }
    
}
