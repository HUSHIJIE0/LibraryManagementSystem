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
        // 获取当前登录的用户信息
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
        // 查询当前用户是否已经存在未归还借阅
        BorrowRecord borrowRecord = borrowRecordRepository.queryOneBorrowRecord(bookName, author, user.getUserName());
        if (borrowRecord != null) {
            System.out.println("You have already borrowed one.");
            return;
        }
        // 查询借阅的图书是否存在
        Book book = bookRepository.queryBookByNameAuthor(bookName, author);
        if (book == null) {
            System.out.println("The book not exists.");
            return;
        }
        int inventory = book.getInventory();
        if (inventory > 0) {
            // 先减库存，再登记给用户
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
