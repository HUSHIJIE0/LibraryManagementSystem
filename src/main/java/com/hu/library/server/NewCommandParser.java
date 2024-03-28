package com.hu.library.server;

import com.hu.library.annotation.Command;
import com.hu.library.aspect.CommandHandlerAspect;
import com.hu.library.command.CommandHandler;
import com.hu.library.utils.ReflectionUtils;

import java.util.*;

public class NewCommandParser {

    // 解析并执行命令
    public static void parseCommand(Scanner scanner) {
        Map<String, CommandHandler> commandHandlers = scanCommandHandlers();
        CommandHandlerAspect aspect = new CommandHandlerAspect();
        for (Map.Entry<String, CommandHandler> handlerEntry : commandHandlers.entrySet()) {
            aspect.registerCommandHandler(handlerEntry.getValue());
        }
        while (true) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();
            // 解析命令
            List<String> parts = parseCommand(input);
            if (parts.isEmpty()) {
//                System.out.println("Please enter a command.");
                continue;
            }

            aspect.executeCommand(parts);
        }
    }

    // 获取所有 CommandHandler 的实现类，并将注解的 value 作为命令，建立映射关系
    public static Map<String, CommandHandler> scanCommandHandlers() {
        Map<String, CommandHandler> handlerMap = new HashMap<>();
        try {
            Set<Class<?>> classes = ReflectionUtils.getClassesImplementing(CommandHandler.class);
            for (Class<?> cls : classes) {
                Command annotation = cls.getAnnotation(Command.class);
                if (annotation != null) {
                    String command = annotation.value();
                    CommandHandler handler = (CommandHandler) cls.newInstance();
                    handlerMap.put(command, handler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handlerMap;
    }


    private static List<String> parseCommand(String commandLine) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : commandLine.toCharArray()) {
            if (c == ' ' && !inQuotes) {
                parts.add(sb.toString());
                sb.setLength(0); // 清空StringBuilder
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            parts.add(sb.toString());
        }

        return parts;
    }
}

