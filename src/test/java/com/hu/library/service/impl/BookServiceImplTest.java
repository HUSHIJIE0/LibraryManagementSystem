package com.hu.library.service.impl;

import com.hu.library.repository.impl.XMLBookRepository;
import com.hu.library.repository.impl.XMLBorrowRecordRepository;
import com.hu.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BookServiceImplTest {

    private BookService bookService;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(new XMLBookRepository(), new XMLBorrowRecordRepository());
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testAddBook_NewBook() {
        String bookName = "Test Book";
        String author = "Test Author";
        int inventory = 10;

        bookService.addBook(bookName, author, inventory);
    }

    @Test
    void testAddBook_ExistingBook() {
        String bookName = "Test Book";
        String author = "Test Author";
        int inventory = 10;

        bookService.addBook(bookName, author, inventory);
    }

    @Test
    void testDeleteBook_NotBorrowed() {
        String bookName = "Test Book";
        String author = "Test Author";

        bookService.deleteBook(bookName, author);
    }

    @Test
    void testDeleteBook_Borrowed() {
        String bookName = "Test Book";
        String author = "Test Author";

        bookService.deleteBook(bookName, author);
    }

    @Test
    void testListBooks() {
        bookService.listBooks();
    }

    @Test
    void testSearch_BookFound() {
        String bookName = "Test Book";
        String author = "Test Author";

        bookService.search(bookName, author);
    }

    @Test
    void testSearch_BookNotFound() {
        String bookName = "Test Book";
        String author = "Test Author";

        bookService.search(bookName, author);
    }

}
