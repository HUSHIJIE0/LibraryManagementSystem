package com.hu.library.service;

/**
 * 图示管理类
 */
public interface BookService {
    // 新增图书
    void addBook();
    // 删除图书
    void deleteBook();
    // 查询可借图书
    void listBooks();
    // 查询借阅中图书
    void listBorrowedBooks();
    // 借阅新图书
    void borrowBooks();
    // 归还图书
    void returnBook();




}
