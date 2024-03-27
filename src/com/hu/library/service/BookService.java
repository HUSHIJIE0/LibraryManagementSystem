package com.hu.library.service;

import com.hu.library.entity.Book;

import java.util.List;

/**
 * 图示管理类
 */
public interface BookService {
    // 新增图书
    void addBook(String bookName, String author, int inventory);
    // 删除图书
    void deleteBook(String bookName, String author);
    // 查询可借图书
    void listBooks();
    // 通过书名和作者查询图书
    Book search(String bookName, String author);
    // 更新图书信息
    boolean updateBook(String bookName, int inventory);
    // 通过书名和作者查询图书-基础查询
    Book queryBookByNameAuthor(String bookName, String author);
}
