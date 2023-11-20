package org.eintr.springframework;

public class BeansException extends RuntimeException {
	public BeansException(String msg) {
		super(msg);
	}

	public BeansException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
