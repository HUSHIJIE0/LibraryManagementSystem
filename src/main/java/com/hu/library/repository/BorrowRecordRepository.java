package com.hu.library.repository;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;

import java.util.List;

/**
 * 图书借阅持久化层处理接口，提供基础的图书借阅操作定义
 * 具体实现当前为xml文件，可后续扩展数据库等类型，按需注入业务服务层
 */
public interface BorrowRecordRepository {

    /**
     * 借阅一本图书
     * @param book
     * @return
     */
    boolean addBorrowRecord(Book book);

    /**
     * 查询库存图书的借阅记录
     * @param bookName
     * @param author
     * @return
     */
    List<BorrowRecord> queryOnBorrowRecord(String bookName, String author);

    /**
     * 查询某个用户某种图书的借阅记录
     * @param bookName
     * @param author
     * @param userName
     * @return
     */
    BorrowRecord queryOneBorrowRecord(String bookName, String author, String userName);

    /**
     * 更新借阅记录，归还图书
     * @param borrowRecord
     * @return
     */
    boolean updateBorrowRecord(BorrowRecord borrowRecord);

    /**
     * 查询某用户的所有借阅图书
     * @param userName
     * @return
     */
    List<BorrowRecord> listBorrowedBooks(String userName);
}
