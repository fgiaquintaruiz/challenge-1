package com.fggr.naranjaxchallenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fggr.naranjaxchallenge.enumators.CommerceEnum;
import com.fggr.naranjaxchallenge.enumators.TypeEnum;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class TransactionDto {

    @JsonProperty("account_id")
    private Integer accountId;
    private TypeEnum type;
    private CommerceEnum commerce;
    private BigInteger amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date time;

}
