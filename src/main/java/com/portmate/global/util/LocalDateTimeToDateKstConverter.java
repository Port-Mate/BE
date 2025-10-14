package com.portmate.global.util;


import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@WritingConverter
public class LocalDateTimeToDateKstConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("Asia/Seoul")).toInstant());
    }
}
