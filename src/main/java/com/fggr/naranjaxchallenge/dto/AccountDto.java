package com.fggr.naranjaxchallenge.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDto {

    private Integer id;
    private String name;
    private Integer dni;

    @JsonProperty("active_card")
    private boolean activeCard;

    @JsonProperty(value = "available_amount")
    @JsonDeserialize(using = NumberDeserializers.BigIntegerDeserializer.class)
    private BigInteger availableAmount = new BigInteger("0");

    private List<TransactionDto> transactions = new ArrayList<>();
}
