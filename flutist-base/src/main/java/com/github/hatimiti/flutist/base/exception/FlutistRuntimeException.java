package com.github.hatimiti.flutist.base.exception;

/**
 * Wrapped RuntimeException
 * @author hatimiti
 */
public class FlutistRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FlutistRuntimeException(Throwable t) {
		super(t);
	}
	
}
