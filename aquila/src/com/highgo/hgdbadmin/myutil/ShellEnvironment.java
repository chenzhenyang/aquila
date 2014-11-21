package com.highgo.hgdbadmin.myutil;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.codehaus.groovy.tools.shell.IO;

public final class ShellEnvironment {
	private ShellEnvironment() {
	}

	private static final long DEFAULT_POLL_TIMEOUT = 10000;

	private static boolean verbose = false;
	private static boolean interactive = false;
	private static long pollTimeout = DEFAULT_POLL_TIMEOUT;

	static IO io;

	public static String getEnv(String variable, String defaultValue) {
		String value = System.getenv(variable);
		return value != null ? value : defaultValue;
	}

	public static void setIo(IO ioObject) {
		io = ioObject;
	}

	public static IO getIo() {
		return io;
	}



	public static void setVerbose(boolean newValue) {
		verbose = newValue;
	}

	public static boolean isVerbose() {
		return verbose;
	}

	public static void setInteractive(boolean newValue) {
		interactive = newValue;
	}

	public static boolean isInteractive() {
		return interactive;
	}

	public static void setPollTimeout(long timeout) {
		pollTimeout = timeout;
	}

	public static long getPollTimeout() {
		return pollTimeout;
	}



	public static void println(String str, Object... values) {
		io.out.println(MessageFormat.format(str, values));
	}

	public static void println(String str) {
		io.out.println(str);
	}

	public static void println(Object obj) {
		io.out.println(obj);
	}

	public static void println() {
		io.out.println();
	}

	public static void print(String str) {
		io.out.print(str);
	}

	public static void print(Object obj) {
		io.out.print(obj);
	}

	public static void print(String format, Object... args) {
		io.out.printf(format, args);
	}
}
