package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.server.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Command("help")
public class HelpCommand implements CommandHandler {

    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        try (InputStream inputStream = CommandParser.class.getResourceAsStream("/COMMAND.md");
             BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream))) {
            int content;
            while ((content = fileReader.read()) != -1) {
                System.out.print((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
