package com.myBooks.Exception;

public class BookException extends Exception {

	private static final long serialVersionUID = 1997753363232807009L;

	private Integer errorCode;

	public Integer getErrorCode() {
		return errorCode;
	}

	public BookException()
	{
	}

	public BookException(String message)
	{
		super(message);
	}

	public BookException(Throwable cause)
	{
		super(cause);
	}

	public BookException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public BookException(String message, Integer errorCode)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public BookException(String message, Throwable cause,boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
