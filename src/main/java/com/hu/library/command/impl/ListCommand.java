package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.repository.impl.XMLBookRepository;
import com.hu.library.repository.impl.XMLBorrowRecordRepository;
import com.hu.library.service.BookService;
import com.hu.library.service.impl.BookServiceImpl;

import java.util.List;

@Command("list")
public class ListCommand implements CommandHandler {

    private final BookService bookService = new BookServiceImpl(new XMLBookRepository(), new XMLBorrowRecordRepository());

    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        bookService.listBooks();
    }
}
