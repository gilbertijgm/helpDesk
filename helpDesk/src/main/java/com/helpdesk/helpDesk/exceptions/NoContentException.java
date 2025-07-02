package com.helpdesk.helpDesk.exceptions;

public class NoContentException extends RuntimeException{
    public NoContentException(String message) {
        super(message);
    }
}
//Cuando querés comunicar que la lista existe pero está vacía