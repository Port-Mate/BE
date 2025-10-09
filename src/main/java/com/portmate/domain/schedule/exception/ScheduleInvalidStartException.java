package com.portmate.domain.schedule.exception;

import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

public class ScheduleInvalidStartException extends GlobalException {
    public ScheduleInvalidStartException() {
        super(ErrorStatus.SCHEDULE_INVALID_START_DT);
    }
}
