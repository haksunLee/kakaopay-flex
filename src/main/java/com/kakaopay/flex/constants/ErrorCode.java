package com.kakaopay.flex.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("E001", "Internal Server Error"),
    FLEX_EXPIRATION_ERROR("E002", "해당 뿌리기 건은 유효하지 않거나 마감되었습니다."),
    FLEX_NOT_FOUND("E003", "해당 뿌리기 건을 찾을 수 없습니다."),
    NOT_SAME_ROOM_FAIL("E004", "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    FLEX_STATUS_COMPLETE("E005", "완료된 뿌리기 건 입니다. 더이상 받을 수 있는 돈이 없습니다."),
    USER_IS_CREATE_USER("E006", "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    USER_IS_NOT_CREATE_USER("E007", "뿌린 사람 자신만 조회를 할 수 있습니다."),
    USER_GET_BEFORE_FAIL("E008", "뿌리기 당 한 사용자는 한번만 받을 수 있습니다."),
    USER_IS_NOT_OWNER("E009", "뿌린 사람 자신만 조회를 할 수 있습니다."),
    ;

    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }
}
