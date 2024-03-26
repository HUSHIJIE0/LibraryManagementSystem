package com.hu.library.service;

import com.hu.library.enums.UserType;

/**
 * 图书借阅管理接口
 */
public interface BorrowRecordService {
    // 查询借阅中图书
    void listBorrowedBooks();
    // 借阅新图书
    void borrowBooks();
    // 归还图书
    void returnBook();
}
