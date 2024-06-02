package dev.mutti.simplepaymentsapi.enums;

import lombok.Getter;

@Getter
public enum UserType {

    CUSTOMER,
    MERCHANT;

    private final int id;
    private final String description;

    UserType() {
        this.id = ordinal();
        this.description = name();
    }

    public boolean isMerchant() {
        return this == MERCHANT;
    }
}
