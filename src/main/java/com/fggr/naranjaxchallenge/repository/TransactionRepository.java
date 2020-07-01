package com.fggr.naranjaxchallenge.repository;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fggr.naranjaxchallenge.dao.AccountDao;
import com.fggr.naranjaxchallenge.dao.TransactionDao;
import com.fggr.naranjaxchallenge.enumators.CommerceEnum;
import com.fggr.naranjaxchallenge.enumators.TypeEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "transaction", path = "transaction")
public interface TransactionRepository extends CrudRepository<TransactionDao, Integer> {

    //No debe haber más de 10 transacciones en un intervalo de 2 minutos.: high-frequency
    List<TransactionDao> findByAccountIdAndTimeBefore(Integer accountId, Date timeUntil);

    //No debe haber más de 2 transacciones similares (misma type, commerce,account_id y amount)
    //  en un intervalo de 2 minutos: doubled-transaction
    List<TransactionDao> findByTypeAndCommerceAndAccountIdAndAmountAndTimeBefore(TypeEnum type, CommerceEnum commerce, Integer accountId, BigInteger amount, Date timeUntil);

    //Las transacciones de depósito no pueden exceder los 10.000: allowed-amount-exceeds
    //Las transacciones de extracción no pueden exceder los 5000: allowed-amount-exceeds
    List<TransactionDao> findByAccountIdAndType(Integer accountId, TypeEnum type);

}
