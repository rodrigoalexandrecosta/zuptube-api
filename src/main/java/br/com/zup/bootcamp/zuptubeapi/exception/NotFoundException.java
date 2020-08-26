package br.com.zup.bootcamp.zuptubeapi.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
