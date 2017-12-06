package com.topcontrol.infra;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 877766642822938668L;
	
	public BusinessException(String message, Throwable e) {
		super(message, e);
	}
	public BusinessException(String message) {
		super(message, null);
	}
}