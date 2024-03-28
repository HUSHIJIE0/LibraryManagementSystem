package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;

import java.util.List;

@Command("exit")
public class ExitCommand implements CommandHandler {

    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        System.exit(0);
    }
}
