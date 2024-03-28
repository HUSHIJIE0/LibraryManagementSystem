package com.hu.library.command.impl;

import com.hu.library.annotation.Command;
import com.hu.library.command.CommandHandler;
import com.hu.library.repository.impl.XMLBookRepository;
import com.hu.library.repository.impl.XMLBorrowRecordRepository;
import com.hu.library.service.BorrowRecordService;
import com.hu.library.service.impl.BorrowRecordServiceImpl;

import java.util.List;

@Command("return")
public class ReturnCommand implements CommandHandler {

    private final BorrowRecordService borrowRecordService = new BorrowRecordServiceImpl(new XMLBookRepository(), new XMLBorrowRecordRepository());


    /**
     * @param commands
     */
    @Override
    public void handleCommand(List<String> commands) {
        borrowRecordService.returnBook(commands.get(1), commands.get(2));
    }
}
