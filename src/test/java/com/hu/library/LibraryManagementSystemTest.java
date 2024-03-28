package com.hu.library;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class LibraryManagementSystemTest {

    @Test
    public void testParseCommand_ValidInput() {
        String input = "register user123 pass123";
        commonMethod(input);
    }

    @Test
    public void testParseCommand_CorrectInput() {
        String input = "register user user123 pass123";
        commonMethod(input);
    }

    @Test
    public void testParseCommand_EmptyInput() {
        String input = "";
        commonMethod(input);

    }

    @Test
    public void testParseCommand_InvalidCommand() {
        String input = "invalid command";
        commonMethod(input);
    }

    @Test
    public void testRegister() {
        String input = "register user user123 pass123";
        commonMethod(input);
    }

    @Test
    public void testLogin() {
        String input = "login user123 pass123";
        commonMethod(input);
    }


    private static void commonMethod(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        try {
            LibraryManagementSystem.main(null);
        } catch (Exception e) {

        }
    }
}
