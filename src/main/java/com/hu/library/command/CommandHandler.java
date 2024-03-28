package com.hu.library.command;

import java.util.List;

/**
 * 基础处理器接口
 *  实现类实现此接口，并添加@Command的注解，为后续反射与代理提供服务
 *  实现类在impl目录中，注解的value，即对应具体的命令
 */
public interface CommandHandler {
    void handleCommand(List<String> commands);
}
