package com.hu.library.service.impl;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import com.hu.library.repository.BookRepository;
import com.hu.library.repository.BorrowRecordRepository;
import com.hu.library.repository.impl.XMLBookRepository;
import com.hu.library.repository.impl.XMLBorrowRecordRepository;
import com.hu.library.server.SessionManager;
import com.hu.library.service.BorrowRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BorrowRecordServiceImplTest {

    private BorrowRecordService borrowRecordService;

    @BeforeEach
    void setUp() {
        BookRepository bookRepository = new XMLBookRepository();
        BorrowRecordRepository borrowRecordRepository = new XMLBorrowRecordRepository();
        SessionManager.getInstance().setCurrentUser(new User("testUser", "password", UserType.User)); // 设置当前用户
        borrowRecordService = new BorrowRecordServiceImpl(bookRepository, borrowRecordRepository);
    }

    @Test
    void testListBorrowedBooks() {
        borrowRecordService.listBorrowedBooks();
    }

    @Test
    void testBorrowBooks() {
        borrowRecordService.borrowBooks("Test Book", "Test Author");
    }

    @Test
    void testReturnBook() {
        borrowRecordService.returnBook("Test Book", "Test Author");
    }

    // 其他测试方法类似，可以根据需要添加
}
