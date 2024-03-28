package com.hu.library.server;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import com.hu.library.repository.impl.XMLBookRepository;
import com.hu.library.repository.impl.XMLBorrowRecordRepository;
import com.hu.library.repository.impl.XMLUserRepository;
import com.hu.library.service.BookService;
import com.hu.library.service.BorrowRecordService;
import com.hu.library.service.UserService;
import com.hu.library.service.impl.BookServiceImpl;
import com.hu.library.service.impl.BorrowRecordServiceImpl;
import com.hu.library.service.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandParser {
    // 解析并执行命令
    public static void parseCommand(Scanner scanner) {
        UserService userService = new UserServiceImpl(new XMLUserRepository());
        BookService bookService = new BookServiceImpl(new XMLBookRepository(), new XMLBorrowRecordRepository());
        BorrowRecordService borrowRecordService = new BorrowRecordServiceImpl(new XMLBookRepository(), new XMLBorrowRecordRepository());
        while (true) {
            System.out.println("Enter command: ");
            String input = scanner.nextLine();
            // 解析命令
            List<String> parts = parseCommand(input);
            if (parts.isEmpty()) {
                System.out.println("Please enter a command.");
                continue;
            }
            User currentUser = SessionManager.getInstance().getCurrentUser();

            String command = parts.get(0);
            switch (command) {
                case "register":
                    if (parts.size() < 3) {
                        System.out.println("Usage: register <userType> <userName> <password>");
                        continue;
                    }
                    userService.register(UserType.fromString(parts.get(1)), parts.get(2), parts.get(3));
                    break;
                case "login":
                    if (parts.size() < 3) {
                        System.out.println("Usage: login <userName> <password>");
                        continue;
                    }
                    userService.login(parts.get(1), parts.get(2));
                    break;
                case "add":
                    if (currentUser == null) {
                        System.out.println("Please login first.");
                        continue;
                    }
                    if (currentUser.getUserType() != UserType.Admin) {
                        System.out.println("Your are not admin, no permission!");
                        continue;
                    }
                    if (parts.size() < 3) {
                        System.out.println("Usage: add <title> <author> <quantity>");
                        continue;
                    }
                    bookService.addBook(parts.get(1), parts.get(2), Integer.parseInt(parts.get(3)));
                    break;
                case "list":
                    if (currentUser == null) {
                        System.out.println("Please login first.");
                        continue;
                    }
                    bookService.listBooks();
                    break;
                case "search":
                    if (parts.size() < 3) {
                        System.out.println("Usage: search <name> <author>");
                        continue;
                    }
                    bookService.search(parts.get(1), parts.get(2));
                    break;
                case "borrow":
                    if (parts.size() < 3) {
                        System.out.println("Usage: borrow <name> <author>");
                        continue;
                    }
                    borrowRecordService.borrowBooks(parts.get(1), parts.get(2));
                    break;
                case "return":
                    if (parts.size() < 3) {
                        System.out.println("Usage: return <name> <author>");
                        continue;
                    }
                    borrowRecordService.returnBook(parts.get(1), parts.get(2));
                    break;
                case "delete":
                    if (currentUser == null) {
                        System.out.println("Please login first.");
                        continue;
                    }
                    if (currentUser.getUserType() != UserType.Admin) {
                        System.out.println("Your are not admin, no permission!");
                        continue;
                    }
                    if (parts.size() < 3) {
                        System.out.println("Usage: delete <name> <author>");
                        continue;
                    }
                    bookService.deleteBook(parts.get(1), parts.get(2));
                    break;
                case "help":
                    try (InputStream inputStream = CommandParser.class.getResourceAsStream("/COMMAND.md");
                         BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream))) {
                        int content;
                        while ((content = fileReader.read()) != -1) {
                            System.out.print((char) content);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
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

