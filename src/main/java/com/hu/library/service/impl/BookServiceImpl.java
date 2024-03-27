package com.hu.library.service.impl;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
import com.hu.library.repository.BookRepository;
import com.hu.library.repository.BorrowRecordRepository;
import com.hu.library.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BorrowRecordRepository borrowRecordRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }


    @Override
    public void addBook(String bookName, String author, int inventory) {
        Book book = bookRepository.queryBook(bookName);
        if (book == null) {
            // 用户不存在，新增用户
            if (bookRepository.addOneBook(bookName, author, inventory)) {
                System.out.println("Book" + " \"" + bookName + "\" by" + author + " added successfully, inventory:" + inventory);
            }
        } else {
            int newInventory = inventory + book.getInventory();
            if (bookRepository.updateBook(bookName, newInventory)) {
                System.out.println("Book" + " \"" + bookName + "\" by" + author + " inventory successfully updated, new inventory:" + newInventory);
            }
        }
    }


    @Override
    public void deleteBook(String bookName, String author) {
        List<BorrowRecord> borrowRecords = borrowRecordRepository.queryOnBorrowRecord(bookName, author);
        if (borrowRecords.size() > 0) {
            System.out.println("Cannot delete book \"" + bookName + "\" because it is currently borrowed.");
        } else {
            if (bookRepository.deleteOneBook(bookName, author)) {
                System.out.println("Book \"" + bookName + "\" delete successfully.");
            }
        }
    }

    @Override
    public void listBooks() {
        System.out.println("Book List:");
        List<Book> books = bookRepository.queryAllBooks();
        if (!books.isEmpty()) {
            for (Book book : books) {
                System.out.println(book.getBookName() + " - " + book.getAuthor() + " - Inventory：" + book.getInventory());
            }
        }
    }

    @Override
    public Book search(String bookName, String author) {
        Book book = bookRepository.queryBookByNameAuthor(bookName, author);
        if (book != null) {
            System.out.println(book.getBookName() + " - " + book.getAuthor() + " - Inventory：" + book.getInventory());
        }
        return book;
    }


}
