

public class AquilaException extends RuntimeException {

	private final ErrorCode code;

	public AquilaException(ErrorCode code) {
		super(code.getCode() + ":" + code.getMessage());
		this.code = code;
	}

	public AquilaException(ErrorCode code, String extraInfo) {
		super(code.getCode() + ":" + code.getMessage() + " - " + extraInfo);
		this.code = code;
	}

	public AquilaException(ErrorCode code, Throwable cause) {
		super(code.getCode() + ":" + code.getMessage(), cause);
		this.code = code;
	}

	public AquilaException(ErrorCode code, String extraInfo, Throwable cause) {
		super(code.getCode() + ":" + code.getMessage() + " - " + extraInfo, cause);
		this.code = code;
	}

	public ErrorCode getErrorCode() {
		return code;
	}
}
