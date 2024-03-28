package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.repository.impl.XMLUserRepository;
import com.hu.library.service.UserService;
import com.hu.library.service.impl.UserServiceImpl;

import java.util.List;

@Command("login")
public class LoginCommand implements CommandHandler {

    private final UserService userService = new UserServiceImpl(new XMLUserRepository());

    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        userService.login(commands.get(1), commands.get(2));
    }
}
