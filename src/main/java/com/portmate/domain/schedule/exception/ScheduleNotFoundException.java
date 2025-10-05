package com.portmate.domain.schedule.exception;

import com.portmate.global.response.exception.GlobalException;
import com.portmate.global.response.status.ErrorStatus;

public class ScheduleNotFoundException extends GlobalException {
    public ScheduleNotFoundException() {
        super(ErrorStatus.SCHEDULE_NOT_FOUND);
    }
}
