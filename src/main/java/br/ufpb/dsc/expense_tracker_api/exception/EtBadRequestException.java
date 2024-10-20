package br.ufpb.dsc.expense_tracker_api.exception;

public class EtBadRequestException extends RuntimeException {

    public EtBadRequestException(String message) {
        super(message);
    }

    public EtBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
