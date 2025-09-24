package com.portmate.domain.schedule.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExcelUtil {

    public static boolean isNotExcel(MultipartFile file){
        return !"text/csv".equals(file.getContentType()) && !"application/vnd.ms-excel".equals(file.getContentType());
    }

    public static List<Map<String, String>> parseParameterList(List<String> paramList) {
        return paramList.stream()
                .map(entry -> {
                    String[] splitEntry = entry.split(":");
                    if (splitEntry.length == 2) {
                        return Map.of(splitEntry[0], splitEntry[1]);
                    } else {
                        throw new IllegalArgumentException("Invalid parameter format. Expected 'key:value' format.");
                    }
                })
                .collect(Collectors.toList());
    }

    public static Object parseValue(String value) {
        // 값이 숫자인지 여부를 확인하여 적절한 타입으로 변환
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value); // 소수점이 포함된 경우 double로 변환
            } else {
                return Integer.parseInt(value);  // 정수로 변환
            }
        } catch (NumberFormatException e) {
            return value; // 숫자가 아닌 경우 문자열로 저장
        }
    }

}
