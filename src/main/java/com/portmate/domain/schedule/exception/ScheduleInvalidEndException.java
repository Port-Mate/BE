package com.portmate.domain.schedule.exception;

import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

public class ScheduleInvalidEndException extends GlobalException {
    public ScheduleInvalidEndException() {
        super(ErrorStatus.SCHEDULE_INVALID_END_DT);
    }
}
