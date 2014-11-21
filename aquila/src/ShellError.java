
public enum ShellError implements ErrorCode {

	/** An unknown error has occurred. */
	SHELL_0000("An unknown error has occurred"),

	/** The specified command is not recognized. */
	SHELL_0001("The specified command is not recognized"),

	/** The specified function is not recognized. */
	SHELL_0002("The specified function is not recognized"),

	/** An error has occurred when parsing options. */
	SHELL_0003("An error has occurred when parsing options"),

	/** Unable to resolve the variables. */
	SHELL_0004("Unable to resolve the variables"),

	/** We're not able to get user input */
	SHELL_0005("Can't get user input"),

	/** There occurred exception on server side **/
	SHELL_0006("Server has returned exception"),

	/** Command not compatible with batch mode */
	SHELL_0007("Command not compatible with batch mode"),

	/** Job Submission : Cannot sleep */
	SHELL_0008("Cannot sleep"),

	;

	private final String message;

	private ShellError(String message) {
		this.message = message;
	}

	public String getCode() {
		return name();
	}

	public String getMessage() {
		return message;
	}
}
