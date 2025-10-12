package com.portmate.global.response.status;

import com.portmate.global.response.BaseStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseStatusCode {
    // 일반 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403", "금지된 요청입니다."),

    // JWT
    EMPTY_JWT(HttpStatus.UNAUTHORIZED, "COMMON_404", "토큰이 비어있습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "COMMON_405", "유효하지 않은 토큰입니다."),

    // USER
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_001", "이미 존재하는 사용자이며, 비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_002", "해당 사용자를 찾을 수 없습니다."),

    // Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_001", "조건을 만족하는 일정을 찾을 수 없습니다."),
    SCHEDULE_INVALID_START_DT(HttpStatus.BAD_REQUEST, "SCHEDULE_002", "엑셀에 포함된 ETA 날짜 중 스케줄 시작 일자 범위에 포함되지 않는 값이 있습니다."),
    SCHEDULE_INVALID_END_DT(HttpStatus.BAD_REQUEST, "SCHEDULE_003", "엑셀에 포함된 ETD 날짜 중 스케줄 종료 일자 범위에 포함되지 않는 값이 있습니다."),

    // FILE
    FILE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "FILE_001", "지원하지 않는 파일 형식입니다."),

    //Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTIFICATION_001", "해당 알림을 찾을 수 없습니다."),

    // Berth Change
    INVALID_BERTH_PLACEMENT(HttpStatus.BAD_REQUEST, "BERTH_CHANGE_001", "유효하지 않은 선석 배치입니다."),
    CHANGE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "BERTH_CHANGE_002", "변경 요청을 찾을 수 없습니다."),
    CANNOT_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, "BERTH_CHANGE_003", "취소할 수 없는 상태의 요청입니다."),

    // Schedule Version Review
    UNAUTHORIZED_REVIEW(HttpStatus.FORBIDDEN, "VERSION_REVIEW_001", "해당 버전을 승인/거부할 권한이 없습니다."),
    VERSION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "VERSION_REVIEW_002", "이미 처리된 버전입니다."),
    VERSION_NOT_APPROVED(HttpStatus.BAD_REQUEST, "VERSION_REVIEW_003", "승인되지 않은 버전은 스케줄에 적용할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
