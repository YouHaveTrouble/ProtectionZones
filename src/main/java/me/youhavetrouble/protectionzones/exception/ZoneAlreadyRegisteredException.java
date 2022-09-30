package me.youhavetrouble.protectionzones.exception;

public class ZoneAlreadyRegisteredException extends IllegalArgumentException {

    public ZoneAlreadyRegisteredException(String message) {
        super(message);
    }

}
