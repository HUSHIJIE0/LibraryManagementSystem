package com.hu.library.service;

import com.hu.library.entity.Book;

/**
 * 图示管理类
 */
public interface BookService {
    /**
     * 新增图书
     */
    void addBook(String bookName, String author, int inventory);

    /**
     * 删除图书
     * @param bookName
     * @param author
     */
    void deleteBook(String bookName, String author);

    /**
     * 查询可借图书
     */
    void listBooks();

    /**
     * 通过书名和作者查询图书
     * @param bookName
     * @param author
     * @return
     */
    Book search(String bookName, String author);

}
