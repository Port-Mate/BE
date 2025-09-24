package com.portmate.domain.schedule.exception;

import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

public class FileNotSupportException extends GlobalException {
    public FileNotSupportException() {
        super(ErrorStatus.FILE_NOT_SUPPORTED);
    }
}
