import com.highgo.hgdbadmin.myutil.ShellEnvironment;

import groovy.lang.MissingPropertyException;


/**
 * Pretty printing of Throwable objects
 */
public class ThrowableDisplayer {

	/**
	 * Error hook installed to Groovy shell.
	 *
	 * Will display exception that appeared during executing command. In most
	 * cases we will simply delegate the call to printing throwable method,
	 * however in case that we've received ClientError.CLIENT_0006 (server
	 * exception), we will unwrap server issue and view only that as local
	 * context shouldn't make any difference.
	 *
	 * @param t
	 *            Throwable to be displayed
	 */
	public static void errorHook(Throwable t) {
		ShellEnvironment.println("@|red Exception has occurred during processing command |@");

		// If this is server exception from server
		if (t instanceof AquilaException && ((AquilaException) t).getErrorCode() == ShellError.SHELL_0006) {
			ShellEnvironment.print("@|red Server has returned exception: |@");
			printThrowable(t.getCause(), ShellEnvironment.isVerbose());
		} else if (t.getClass() == MissingPropertyException.class) {
			ShellEnvironment.print("@|red Unknown command: |@");
			ShellEnvironment.println(t.getMessage());
		} else {
			printThrowable(t, ShellEnvironment.isVerbose());
		}
	}

	/**
	 * Pretty print Throwable instance including stack trace and causes.
	 *
	 * @param t
	 *            Throwable to display
	 */
	protected static void printThrowable(Throwable t, boolean verbose) {
		ShellEnvironment.print("@|red Exception: |@");
		ShellEnvironment.print(t.getClass().getName());
		ShellEnvironment.print(" @|red Message: |@");
		ShellEnvironment.print(t.getMessage());
		ShellEnvironment.println();

		if (verbose) {
			ShellEnvironment.println("Stack trace:");
			for (StackTraceElement e : t.getStackTrace()) {
				ShellEnvironment.print("\t @|bold at |@ ");
				ShellEnvironment.print(e.getClassName());
				ShellEnvironment.print(" (@|bold " + e.getFileName() + ":" + e.getLineNumber() + ") |@ ");
				ShellEnvironment.println();
			}

			Throwable cause = t.getCause();
			if (cause != null) {
				ShellEnvironment.print("Caused by: ");
				printThrowable(cause, verbose);
			}
		}
	}

	private ThrowableDisplayer() {
		// Instantiation is prohibited
	}
}
