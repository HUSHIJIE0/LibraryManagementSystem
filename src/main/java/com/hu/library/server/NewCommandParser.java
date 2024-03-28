package com.hu.library.server;

import com.hu.library.annotation.Command;
import com.hu.library.aspect.CommandHandlerAspect;
import com.hu.library.command.CommandHandler;
import com.hu.library.utils.ReflectionUtils;

import java.util.*;

/**
 * 新版本命令解析器
 * 利用注解，反射，动态代理，分命令处理器等，实现命令入口的统一处理
 * 新增命令，只需新增对应处理器，映射枚举等即可
 */
public class NewCommandParser {

    // 解析并执行命令
    public static void parseCommand(Scanner scanner) {
        // 获取所有命令处理器的映射关系
        Map<String, CommandHandler> commandHandlers = scanCommandHandlers();
        // 创建代理服务并注册代理
        CommandHandlerAspect aspect = new CommandHandlerAspect();
        for (Map.Entry<String, CommandHandler> handlerEntry : commandHandlers.entrySet()) {
            aspect.registerCommandHandler(handlerEntry.getValue());
        }
        while (true) {
            System.out.print(">");
            String input = scanner.nextLine();
            // 解析命令
            List<String> parts = parseCommand(input);
            if (parts.isEmpty()) {
//                System.out.println("Please enter a command.");
                continue;
            }
            // 代理代为执行命令
            aspect.executeCommand(parts);
        }
    }

    /**
     *  获取所有 CommandHandler 的实现类，并将注解的 value 作为命令，建立映射关系
     * @return
     */
    public static Map<String, CommandHandler> scanCommandHandlers() {
        Map<String, CommandHandler> handlerMap = new HashMap<>();
        try {
            Set<Class<?>> classes = ReflectionUtils.getClassesImplementing(CommandHandler.class);
            for (Class<?> cls : classes) {
                Command annotation = cls.getAnnotation(Command.class);
                if (annotation != null) {
                    String command = annotation.value();
                    CommandHandler handler = (CommandHandler) cls.getDeclaredConstructor().newInstance();
                    handlerMap.put(command, handler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handlerMap;
    }

    /**
     * 输入命令解析，解决输入命令包含引号，且参数中间有空格的情况
     * @param commandLine
     * @return
     */
    private static List<String> parseCommand(String commandLine) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        boolean foundFirstChar = false;

        for (char c : commandLine.toCharArray()) {
            if (!foundFirstChar && c != ' ') {
                foundFirstChar = true;
            }
            if (foundFirstChar) {
                if (c == '"' && !inQuotes) {
                    inQuotes = true;
                } else if (c == '"' && inQuotes) {
                    inQuotes = false;
                }
                if (c == ' ' && !inQuotes) {
                    if (sb.length() > 0) {
                        parts.add(sb.toString());
                        sb.setLength(0); // 清空StringBuilder
                    }
                } else {
                    sb.append(c);
                }
            }
        }

        if (!sb.isEmpty()) {
            parts.add(sb.toString());
        }

        return parts;
    }
}

