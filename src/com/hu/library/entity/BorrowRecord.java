package com.hu.library.entity;

/**
 * 图书借阅记录
 */
public class BorrowRecord {
    // 用户
    private String username;
    // 是否借阅
    private String isbn;
    // 借出时间
    private String borrowDate;
    // 归还时间
    private String returnDate;

    public BorrowRecord(String username, String isbn, String borrowDate, String returnDate) {
        this.username = username;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
