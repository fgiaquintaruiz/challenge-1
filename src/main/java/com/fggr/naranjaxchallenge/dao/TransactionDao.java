package com.fggr.naranjaxchallenge.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fggr.naranjaxchallenge.enumators.CommerceEnum;
import com.fggr.naranjaxchallenge.enumators.TypeEnum;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity(name = "TRANSACTION")
public class TransactionDao extends RepresentationModel<TransactionDao> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;

    private Integer accountId;
    @Enumerated
    private TypeEnum type;
    @Enumerated
    private CommerceEnum commerce;
    private BigInteger amount;
    private Date time;

}
