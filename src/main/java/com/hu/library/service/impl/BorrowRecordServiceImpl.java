package com.hu.library.service.impl;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
import com.hu.library.entity.User;
import com.hu.library.repository.BookRepository;
import com.hu.library.repository.BorrowRecordRepository;
import com.hu.library.server.SessionManager;
import com.hu.library.service.BorrowRecordService;

import java.util.List;

public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BookRepository bookRepository;

    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowRecordServiceImpl(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public void listBorrowedBooks() {
        User user = SessionManager.getInstance().getCurrentUser();
        List<BorrowRecord> borrowRecords = borrowRecordRepository.listBorrowedBooks(user.getUserName());
        System.out.println("myList:");
        for (BorrowRecord record : borrowRecords) {
            System.out.println(record.getBookName() + " - " + record.getAuthor() + " - borrowDate：" + record.getBorrowDate());
        }
    }

    @Override
    public void borrowBooks(String bookName, String author) {
        User user = SessionManager.getInstance().getCurrentUser();
        BorrowRecord borrowRecord = borrowRecordRepository.queryOneBorrowRecord(bookName, author, user.getUserName());
        if (borrowRecord != null) {
            System.out.println("You have already borrowed one.");
            return;
        }
        Book book = bookRepository.queryBookByNameAuthor(bookName, author);
        int inventory = book.getInventory();
        if (inventory > 0) {
            if (bookRepository.updateBook(bookName, inventory - 1)) {
                if (borrowRecordRepository.addBorrowRecord(book)) {
                    System.out.println("Book \"" + bookName + "\" " + " successfully borrowed.");
                }
            }
        } else {
            System.out.println("The inventory is insufficient, borrowing failed.");
        }

    }

    @Override
    public void returnBook(String bookName, String author) {
        User user = SessionManager.getInstance().getCurrentUser();
        BorrowRecord borrowRecord = borrowRecordRepository.queryOneBorrowRecord(bookName, author, user.getUserName());
        if (borrowRecord == null) {
            System.out.println("The book has not been borrowed");
        } else {
            Book book = bookRepository.queryBookByNameAuthor(bookName, author);
            int inventory = book.getInventory();
            if (bookRepository.updateBook(bookName, inventory + 1)) {
                if (borrowRecordRepository.updateBorrowRecord(borrowRecord)) {
                    System.out.println("Book \"" + bookName + "\" " + " successfully returned.");
                }
            }
        }
    }

}
