package com.devblok.kpc.entity.dto;

public enum AuthStatus {
    SUCCESS(0),
    NO_SUCH_USER(1),
    INCORRECT_PASSWORD(2),
    DEACTIVATED_USER(3),
    UNKNOWN_ERROR(4);

    private int status;
    AuthStatus(int status) {
        this.status = status;
    }
}
