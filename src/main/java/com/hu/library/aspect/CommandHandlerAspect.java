package com.hu.library.aspect;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.entity.User;
import com.hu.library.enums.CommandValidate;
import com.hu.library.enums.UserType;
import com.hu.library.server.SessionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 命令处理的切面，代理实现
 *  负责处理权限，命令验证等工作
 */
public class CommandHandlerAspect {
    // 命令与对应处理器映射
    private final Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
    // 允许匿名访问的命令，简单实现
    private static final Set<String> anonymousCommands = new HashSet<>();
    // 只允许admin用户访问的命令
    private static final Set<String> adminCommands = new HashSet<>();


    public CommandHandlerAspect() {
        // 允许的命令
        anonymousCommands.add("register");
        anonymousCommands.add("login");
        anonymousCommands.add("list");
        anonymousCommands.add("help");
        anonymousCommands.add("exit");
        adminCommands.add("add");
        adminCommands.add("delete");
    }

    /**
     * 解析命令处理器的注解，初始化命令与处理器映射关系
     * @param commandHandler
     */
    public void registerCommandHandler(CommandHandler commandHandler) {
        Class<? extends CommandHandler> handlerClass = commandHandler.getClass();
        Command annotation = handlerClass.getAnnotation(Command.class);
        if (annotation != null) {
            commandHandlerMap.put(annotation.value(), commandHandler);
        }
    }

    /**
     * 创建处理的代理
     * @param commandHandler
     * @return
     */
    public CommandHandler createProxy(CommandHandler commandHandler) {
        return (CommandHandler) Proxy.newProxyInstance(
                commandHandler.getClass().getClassLoader(),
                commandHandler.getClass().getInterfaces(),
                new CommandHandlerInvocationHandler(commandHandler));
    }

    /**
     * 代理执行输入命令
     * @param commands
     */
    public void executeCommand(List<String> commands) {
        CommandHandler commandHandler = commandHandlerMap.get(commands.getFirst());
        if (commandHandler != null) {
            CommandHandler proxyHandler = createProxy(commandHandler);
            proxyHandler.handleCommand(commands);
        } else {
            System.out.println("Unknown command: " + commands.getFirst());
            System.out.println("For help please enter: help" );
        }
    }

    /**
     * 调用处理器
     */
    private static class CommandHandlerInvocationHandler implements InvocationHandler {
        private final CommandHandler target;

        public CommandHandlerInvocationHandler(CommandHandler target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (!permissionValidation(args)) return null;
            if (!commandValidation(args)) return null;
            return method.invoke(target, args);
        }

        /**
         * 命令权限认证
         * @param args
         * @return
         */
        private static boolean permissionValidation(Object[] args) {
            List<String> commands = (List<String>) args[0];
            String command = commands.getFirst();
            if (anonymousCommands.contains(command)) {
                // 允许匿名访问
                return true;
            } else {
                User currentUser = SessionManager.getInstance().getCurrentUser();
                if (currentUser == null) {
                    System.out.println("Please login first.");
                    return false;
                }
                if (adminCommands.contains(command)) {
                    if (currentUser.getUserType() != UserType.Admin) {
                        System.out.println("Your are not admin, no permission!");
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * 命令规范验证
         * @param args
         * @return
         */
        private static boolean commandValidation(Object[] args) {
            List<String> commands = (List<String>) args[0];
            String command = commands.getFirst();
            CommandValidate commandEnum = CommandValidate.getCommandEnum(command);
            if (commands.size() != commandEnum.getExpectedArgsCount()) {
                System.out.println(commandEnum.getErrorMessage());
                return false;
            }
            return true;
        }
    }

}

