package com.example.beimplementation.Exceptions;

import lombok.Getter;

@Getter
public class MultipleEntryException extends Exception{

    private final int foundEntryCount;

    //jpa에서 고유해야하는 동일시간 동일장소의 예측 결과가 두개 이상 발견되었을 떼 발생하는 예외
    public MultipleEntryException(int foundEntryCount) {
        this.foundEntryCount = foundEntryCount;
    }
}
