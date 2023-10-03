package jensostertag.pushserver.util;

public class Logger {
    public static int LOG_LEVEL = 1;
    public String tag;

    public static void setLogLevel(int level) {
        Logger.LOG_LEVEL = level;
    }

    public void error(String message) {
        if(Logger.LOG_LEVEL >= 1) {
            System.out.println(Color.GRAY + "(" + Color.RED + "ERROR" + Color.GRAY + ") [" + Color.YELLOW + this.tag + Color.GRAY + "] " + Color.RED + message + Color.RESET);
        }
    }

    public void log(String message) {
        if(Logger.LOG_LEVEL >= 1) {
            System.out.println(Color.GRAY + "( " + Color.BLUE + "LOG " + Color.GRAY + ") [" + Color.YELLOW + this.tag + Color.GRAY + "] " + Color.RESET + message + Color.RESET);
        }
    }

    public void debug(String message) {
        if(Logger.LOG_LEVEL >= 2) {
            System.out.println(Color.GRAY + "(" + Color.BLUE + "DEBUG" + Color.GRAY + ") [" + Color.YELLOW + this.tag + Color.GRAY + "] " + Color.RESET + message + Color.RESET);
        }
    }

    public Logger() {
        this.tag = "Logger";
    }

    public Logger(String tag) {
        this.tag = tag;
    }
}
