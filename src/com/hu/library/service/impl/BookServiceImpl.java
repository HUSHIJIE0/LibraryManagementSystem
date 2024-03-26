package com.hu.library.service.impl;

import com.hu.library.service.BookService;

public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE = "books_data.xml";

    @Override
    public void addBook(String title, String author, int quantity) {
        System.out.println("addBook");
    }

    @Override
    public void deleteBook() {

    }

    @Override
    public void listBooks() {

    }

}
