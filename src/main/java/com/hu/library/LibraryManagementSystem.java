package com.hu.library;

import com.hu.library.server.NewCommandParser;

import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        System.out.println("Welcome to the Library Management!");
        // 获取输入器
        Scanner scanner = new Scanner(System.in);
        // 进入命令解析循环
//        CommandParser.parseCommand(scanner);
        NewCommandParser.parseCommand(scanner);
        // 关闭 Scanner 对象
        scanner.close();

    }


}
