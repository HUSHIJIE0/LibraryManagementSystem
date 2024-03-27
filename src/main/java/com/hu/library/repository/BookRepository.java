package com.hu.library.repository;

import com.hu.library.entity.Book;

import java.util.List;

public interface BookRepository {
    Book queryBook(String bookName);

    Book queryBookByNameAuthor(String bookName, String author);

    List<Book> queryAllBooks();

    boolean addOneBook(String bookName, String author, int inventory);

    boolean updateBook(String bookName, int inventory);

    boolean deleteOneBook(String bookName, String author);


}
