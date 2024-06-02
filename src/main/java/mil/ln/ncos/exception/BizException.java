package mil.ln.ncos.exception;

import mil.ln.ncos.cmmn.error.ErrorCode;

public class BizException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6203431917337335314L;
	/**
	 * 
	 */
	private final ErrorCode errorCode;

	public BizException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public BizException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
