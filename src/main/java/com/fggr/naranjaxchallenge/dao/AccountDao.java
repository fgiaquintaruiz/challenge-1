package com.fggr.naranjaxchallenge.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "ACCOUNT")
public class AccountDao extends RepresentationModel<AccountDao> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Integer dni;
    private boolean activeCard;
    private BigInteger availableAmount = new BigInteger("0");
    @OneToMany(targetEntity = TransactionDao.class, cascade=CascadeType.ALL)
    private List<TransactionDao> transactions = new ArrayList<>();
}
