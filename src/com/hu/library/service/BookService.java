package com.hu.library.service;

/**
 * 图示管理类
 */
public interface BookService {
    // 新增图书
    void addBook(String title, String author, int quantity);
    // 删除图书
    void deleteBook();
    // 查询可借图书
    void listBooks();
}
