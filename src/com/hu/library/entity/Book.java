package com.hu.library.entity;

/**
 * 图书实体类
 * 包含图书的基本信息，图书借阅状态，当前借阅人等信息
 */
public class Book {
    // 书名
    private String bookName;
    // 作者
    private String author;
    // 库存数量
    private int inventory;

    public Book(String bookName, String author, int inventory) {
        this.bookName = bookName;
        this.author = author;
        this.inventory = inventory;
    }

    public Book() {

    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
