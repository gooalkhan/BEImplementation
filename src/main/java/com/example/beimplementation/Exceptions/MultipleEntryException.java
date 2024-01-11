package com.example.beimplementation.Exceptions;

import lombok.Getter;

@Getter
public class MultipleEntryException extends Exception{

    private final int foundEntry;

    public MultipleEntryException(int foundEntry) {
        this.foundEntry = foundEntry;
    }
}
