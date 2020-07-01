package com.fggr.naranjaxchallenge.service;

import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dao.TransactionDao;
import com.fggr.naranjaxchallenge.dto.ErrorResponse;
import com.fggr.naranjaxchallenge.enumators.TypeEnum;
import com.fggr.naranjaxchallenge.repository.AccountRepository;
import com.fggr.naranjaxchallenge.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public ErrorResponse validateOperation(AccountDao accountDao, TransactionDao transactionDao) {

        ErrorResponse error = new ErrorResponse();
        error.setCode(400);
        error.setMessage("rules have not passed");

        List<String> violations = new ArrayList<>();
        if(transactionDao == null){
            //consulta duplicado
            final List<AccountDao> findByDniAndName = accountRepository.findByDniAndName(accountDao.getDni(), accountDao.getName());

            if(findByDniAndName != null && findByDniAndName.size() > 0) {
                violations.add("account-already-created");
            }
        }

        if(transactionDao != null){

            //El monto de la transacción no debe exceder el límite disponible: insufficient-amount
            if(!TypeEnum.DEPOSIT.equals(transactionDao.getType()) && transactionDao.getAmount().compareTo(accountDao.getAvailableAmount()) > 0) {
                violations.add("insufficient-amount");
            }

            //No debe haber más de 10 transacciones en un intervalo de 2 minutos.: high-frequency
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -2);
            Date until = calendar.getTime();

            List<TransactionDao> ruleHighFrequency = transactionRepository.findByAccountIdAndTimeBefore(accountDao.getId(), until);

            if(ruleHighFrequency.size() > 10) {
                violations.add("high-frequency");
            }
            //No debe haber más de 2 transacciones similares (misma type, commerce,account_id y amount)
            //  en un intervalo de 2 minutos: doubled-transaction
            List<TransactionDao> ruleDoubledTransaction = transactionRepository.findByTypeAndCommerceAndAccountIdAndAmountAndTimeBefore(transactionDao.getType(), transactionDao.getCommerce(), transactionDao.getAccountId(), transactionDao.getAmount(), until);
            if(ruleDoubledTransaction.size() > 1) {
                violations.add("doubled-transaction");
            }
            //Las transacciones de depósito no pueden exceder los 10.000: allowed-amount-exceeds
            List<TransactionDao> ruleAllowedAmountDepositList = transactionRepository.findByAccountIdAndType(transactionDao.getAccountId(), TypeEnum.DEPOSIT);

            BigInteger ruleAllowedAmountDeposit = ruleAllowedAmountDepositList.stream()
                    .map(TransactionDao::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigInteger.ZERO, BigInteger::add);
            ruleAllowedAmountDeposit = ruleAllowedAmountDeposit.add(transactionDao.getAmount());

            //Las transacciones de extracción no pueden exceder los 5000: allowed-amount-exceeds
            List<TransactionDao> ruleAllowedAmountWithdrawList = transactionRepository.findByAccountIdAndType(transactionDao.getAccountId(), TypeEnum.WITHDRAW);

            BigInteger ruleAllowedAmountWithdraw = ruleAllowedAmountWithdrawList.stream()
                    .map(TransactionDao::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigInteger.ZERO, BigInteger::add);
            ruleAllowedAmountWithdraw = ruleAllowedAmountWithdraw.add(transactionDao.getAmount());

            if(new BigInteger("10000").compareTo(ruleAllowedAmountDeposit) < 0 || new BigInteger("5000").compareTo(ruleAllowedAmountWithdraw) < 0) {
                violations.add("allowed-amount-exceeds");
            }
        }
        error.setViolations(violations);

        return error;
    }
}
