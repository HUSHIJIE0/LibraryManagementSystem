package com.hu.library.repository;

import com.hu.library.entity.Book;

import java.util.List;

/**
 * 图书持久化层处理接口，提供基础的图书操作定义
 * 具体实现当前为xml文件，可后续扩展数据库等类型，按需注入业务服务层
 */
public interface BookRepository {

    /**
     * 根据用户名和作者 查询图书信息
     * @param bookName
     * @param author
     * @return
     */
    Book queryBookByNameAuthor(String bookName, String author);

    /**
     * 查询所有可借用的图书
     * @return
     */
    List<Book> queryAllBooks();

    /**
     * 新增一种图书
     * @param bookName
     * @param author
     * @param inventory
     * @return
     */
    boolean addOneBook(String bookName, String author, int inventory);

    /**
     * 更新图书库存
     * @param bookName
     * @param inventory
     * @return
     */
    boolean updateBook(String bookName, int inventory);

    /**
     * 删除图书信息
     * @param bookName
     * @param author
     * @return
     */
    boolean deleteOneBook(String bookName, String author);


}
