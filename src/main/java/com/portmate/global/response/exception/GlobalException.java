package com.portmate.global.response.exception;

import com.portmate.global.response.BaseStatusCode;
import com.portmate.global.response.ErrorDetail;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseStatusCode code;

    public GlobalException(BaseStatusCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ErrorDetail getErrorDetail() {
        return ErrorDetail.from(this.code);
    }

}