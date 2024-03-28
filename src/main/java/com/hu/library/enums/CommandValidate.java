package com.hu.library.enums;

public enum CommandValidate {
    REGISTER("register", 4, "Usage: register <userType> <username> <password>"),
    LOGIN("login", 3, "Usage: login <username> <password>"),
    ADD("add", 4, "Usage: add <name> <author> <quantity>"),
    LIST("list", 1, "Usage: list"),
    SEARCH("search", 3, "Usage: Usage: search <name> <author>"),
    BORROW("borrow", 3, "Usage: borrow <name> <author>"),
    RETURN("return", 3, "Usage: return <name> <author>"),
    DELETE("delete", 3, "Usage: delete <name> <author>"),
    MYBORROW("myBorrow", 1, "Usage: myBorrow"),

    HELP("help", 1, "Usage: help"),
    EXIT("exit", 1, ""),

    OTHER("other", 0, "error command."),

    ;

    private final String command;
    private final int expectedArgsCount;
    private final String errorMessage;

    CommandValidate(String command, int expectedArgsCount, String errorMessage) {
        this.command = command;
        this.expectedArgsCount = expectedArgsCount;
        this.errorMessage = errorMessage;
    }

    public String getCommand() {
        return command;
    }

    public int getExpectedArgsCount() {
        return expectedArgsCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    // 根据命令字符串找到对应的枚举值
    public static CommandValidate getCommandEnum(String commandStr) {
        for (CommandValidate command : CommandValidate.values()) {
            if (command.getCommand().equalsIgnoreCase(commandStr)) {
                return command;
            }
        }
        return null; // 如果找不到对应的枚举值，则返回null
    }


}
