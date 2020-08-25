package com.gura.spring05.exception;

import org.springframework.dao.DataAccessException;

public class NoDeliveryException extends DataAccessException{
	/*
	 * 트랜잭션 관리에 영향을 주는 예외를 발생시키기 위해서 DataAccessException
	 * 클래스를 상속받아서 클래스를 정의한다.
	 * 
	 */
	public NoDeliveryException(String msg) {
		super(msg);
		
	}

}
