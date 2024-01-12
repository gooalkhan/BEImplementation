package com.example.beimplementation.Exceptions;

import lombok.Getter;

@Getter
public class BadQueryException extends Exception {
    private final String resultCode; //기상청 결과코드 00이 아니면 에러 - 문서화되어있지 않아 전부 확인 불가능
    private final String resultMsg; //기상청 결과메시지, NORMAL_SERVICE 아니면 에러

    //클라이언트에서 잘못된 API요청 전달시, 서버에서 걸러낼 수 없을 경우 기상청 API에 확인해 잘못된 요청이라고 확인될 경우에
    //발생하는 예외
    public BadQueryException(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
