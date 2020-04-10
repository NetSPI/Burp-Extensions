package extensionTemplate;

import burp.IBurpExtenderCallbacks;

/*
Just a simple logger for use throughout an extension.
 */
public class Logger {
	private static IBurpExtenderCallbacks callbacks = null;

	public static void init(IBurpExtenderCallbacks callbacks) {
		Logger.callbacks = callbacks;
	}

	public static void log(String msg) {
		if (Logger.callbacks != null) {
			callbacks.printOutput(msg);
		}
	}

	public static void err(String msg) {
		if (Logger.callbacks != null) {
			callbacks.printError(msg);
		}
	}
}
