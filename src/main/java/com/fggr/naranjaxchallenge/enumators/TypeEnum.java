package com.fggr.naranjaxchallenge.enumators;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeEnum {
        @JsonProperty("withdraw")
        WITHDRAW ("withdraw"),
        @JsonProperty("deposit")
        DEPOSIT ("deposit"),
        @JsonProperty("purchase")
        PURCHASE ("purchase");

    private final String name;

    private TypeEnum(String s) {
        name = s;
    }

}
