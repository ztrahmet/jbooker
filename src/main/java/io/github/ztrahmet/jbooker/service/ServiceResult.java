package io.github.ztrahmet.jbooker.service;

import lombok.Getter;

@Getter
public class ServiceResult {

    private final boolean success;
    private final String message;

    public ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ServiceResult success(String message) {
        return new ServiceResult(true, message);
    }

    public static ServiceResult failure(String message) {
        return new ServiceResult(false, message);
    }
}
