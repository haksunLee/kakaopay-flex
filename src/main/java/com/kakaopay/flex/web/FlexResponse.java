package com.kakaopay.flex.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaopay.flex.constants.ErrorCode;
import com.kakaopay.flex.constants.ResponseCode;
import com.kakaopay.flex.exception.FlexException;
import com.kakaopay.flex.exception.dto.ErrorResponseDto;
import lombok.*;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlexResponse {
	private String result;
	private String code;
	private String message;
	private Object data;


	public FlexResponse(ErrorCode errorCode){
		result = ResponseCode.FAIL;
		code = errorCode.getCode();
		message = errorCode.getMessage();
	}
	public FlexResponse(ErrorResponseDto dto){
		result = ResponseCode.FAIL;
		code = dto.getCode();
		message = dto.getMessage();
	}

	public FlexResponse(Object data){
		result = ResponseCode.SUCCESS;
		this.data = data;
	}

	public static FlexResponse of(FlexException e){
		return new FlexResponse(e);
	}
	public static FlexResponse of(Exception e){
		return new FlexResponse(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}