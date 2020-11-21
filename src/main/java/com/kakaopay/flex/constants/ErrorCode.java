package com.kakaopay.flex.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(500, null, "유효하지 않은 양식의 요청 입니다."),
    FLEX_EXPIRATION(400, "C001", "해당 뿌리기 건은 유효하지 않거나 마감되었습니다."),
    FLEX_NOT_FOUND(404, "C002", "해당 뿌리기 건을 찾을 수 없습니다."),
    NOT_INCLUDE_SAME_ROOM(401, "C003", "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    FLEX_COMPLETE_ALREADY(405, "C004", "완료된 뿌리기 건 입니다. 더이상 받을 수 있는 돈이 없습니다."),
    USER_IS_CREATE_USER(401, "C005", "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    USER_IS_NOT_CREATE_USER(401, "C006", "뿌린 사람 자신만 조회를 할 수 있습니다."),
    USER_GET_BEFORE_ALREADY(401, "C007", "뿌리기 당 한 사용자는 한번만 받을 수 있습니다."),
    USER_IS_NOT_OWNER(401, "C008", "뿌린 사람 자신만 조회를 할 수 있습니다."),
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
