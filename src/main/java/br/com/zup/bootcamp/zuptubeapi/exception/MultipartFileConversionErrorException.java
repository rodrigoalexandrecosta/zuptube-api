package br.com.zup.bootcamp.zuptubeapi.exception;

public class MultipartFileConversionErrorException extends RuntimeException {

    public MultipartFileConversionErrorException(String msg, Throwable t) {
        super(msg, t);
    }

    public MultipartFileConversionErrorException(String msg) {
        super(msg);
    }
}