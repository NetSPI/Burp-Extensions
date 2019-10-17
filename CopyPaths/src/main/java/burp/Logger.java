package burp;

import java.io.PrintWriter;

public class Logger {
    private static PrintWriter pw;

    public static void init(PrintWriter pw) {
        Logger.pw = pw;
    }

    public static void log(String message) {
        Logger.pw.println( message);
    }
}
