package com.hu.library.repository;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;

import java.util.List;

public interface BorrowRecordRepository {
    boolean addBorrowRecord(Book book);

    List<BorrowRecord> queryOnBorrowRecord(String bookName, String author);

    BorrowRecord queryOneBorrowRecord(String bookName, String author, String userName);

    boolean updateBorrowRecord(BorrowRecord borrowRecord);

    List<BorrowRecord> listBorrowedBooks(String userName);
}
