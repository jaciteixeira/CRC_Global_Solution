package edu.opengroup.crc.exception;

public class UsernameInactiveException extends RuntimeException{

//    private static final long serialVersionUID = 1L;

    public UsernameInactiveException(String message){
        super(message);
    }
}
