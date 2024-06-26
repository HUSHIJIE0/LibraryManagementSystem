package com.hu.library.service;

/**
 * 图书借阅管理接口
 */
public interface BorrowRecordService {
    /**
     * 查询借阅中图书
     */
    void listBorrowedBooks();

    /**
     * 借阅新图书
     * @param bookName
     * @param author
     */
    void borrowBooks(String bookName, String author);

    /**
     * 归还图书
     * @param bookName
     * @param author
     */
    void returnBook(String bookName, String author);
}
