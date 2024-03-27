package com.hu.library.service;

import com.hu.library.entity.BorrowRecord;

import java.util.List;

/**
 * 图书借阅管理接口
 */
public interface BorrowRecordService {
    // 查询借阅中图书
    void listBorrowedBooks();
    // 借阅新图书
    void borrowBooks(String bookName, String author);
    // 归还图书
    void returnBook(String bookName, String author);
    // 查询借用中记录
    List<BorrowRecord> queryOnBorrow(String bookName, String author);
}
