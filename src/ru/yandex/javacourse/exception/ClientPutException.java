package ru.yandex.javacourse.exception;

public class ClientPutException extends RuntimeException {

    public ClientPutException(final String message) {
        super(message);
    }
}