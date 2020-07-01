package com.fggr.naranjaxchallenge.enumators;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CommerceEnum {

    @JsonProperty("pago_facil")
    PAGO_FACIL ("pago_facil"),
    @JsonProperty("red_link")
    RED_LINK ("red_link"),
    @JsonProperty("burger_king")
    BURGER_KING ("burger_king");

    private final String name;

    private CommerceEnum(String s) {
        name = s;
    }

}
