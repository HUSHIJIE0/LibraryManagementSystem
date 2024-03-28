package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.enums.UserType;
import com.hu.library.repository.impl.XMLUserRepository;
import com.hu.library.service.UserService;
import com.hu.library.service.impl.UserServiceImpl;

import java.util.List;

@Command("register")
public class RegisterCommand implements CommandHandler {

    private final UserService userService = new UserServiceImpl(new XMLUserRepository());

    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        try {
            userService.register(UserType.fromString(commands.get(1)), commands.get(2), commands.get(3));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
