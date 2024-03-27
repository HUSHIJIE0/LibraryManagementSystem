package com.hu.library.entity;

/**
 * 图书借阅记录
 */
public class BorrowRecord {
    // 用户
    private String userName;
    // 图书名
    private String bookName;
    // 借出时间
    private String borrowDate;
    // 是否归还
    private String isReturned;
    // 归还时间
    private String returnDate;

    public BorrowRecord(String userName, String bookName, String borrowDate, String isReturned, String returnDate) {
        this.userName = userName;
        this.bookName = bookName;
        this.isReturned = isReturned;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public BorrowRecord() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(String isReturned) {
        this.isReturned = isReturned;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
