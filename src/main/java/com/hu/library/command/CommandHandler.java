package com.hu.library.command;

import java.util.List;

public interface CommandHandler {
    void handleCommand(List<String> commands);
}
