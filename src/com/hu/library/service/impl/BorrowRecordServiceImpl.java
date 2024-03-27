package com.hu.library.service.impl;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
import com.hu.library.entity.User;
import com.hu.library.server.SessionManager;
import com.hu.library.server.XMLManager;
import com.hu.library.service.BookService;
import com.hu.library.service.BorrowRecordService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowRecordServiceImpl implements BorrowRecordService {

    private static final String BORROW_RECORDS_FILE = "borrow_records_data.xml";

    private BookService bookService = new BookServiceImpl();

    @Override
    public void listBorrowedBooks() {

    }

    @Override
    public void borrowBooks(String bookName, String author) {
        User user = SessionManager.getInstance().getCurrentUser();
        BorrowRecord borrowRecord = queryOneBorrowRecord(bookName, author, user.getUserName());
        if (borrowRecord != null) {
            System.out.println("You have already borrowed one.");
            return;
        }
        Book book = bookService.queryBookByNameAuthor(bookName, author);
        int inventory = book.getInventory();
        if (inventory > 0) {
            if (bookService.updateBook(bookName, inventory - 1)) {
                if (addBorrowRecord(book)) {
                    System.out.println("Book \"" + bookName + "\" " + " successfully borrowed.");
                }
            }
        } else {
            System.out.println("The inventory is insufficient, borrowing failed.");
        }

    }

    @Override
    public void returnBook(String bookName, String author) {
        User user = SessionManager.getInstance().getCurrentUser();
        BorrowRecord borrowRecord = queryOneBorrowRecord(bookName, author, user.getUserName());
        if (borrowRecord == null) {
            System.out.println("The book has not been borrowed");
        } else {
            Book book = bookService.queryBookByNameAuthor(bookName, author);
            int inventory = book.getInventory();
            if (bookService.updateBook(bookName, inventory + 1)) {
                if (updateBorrowRecord(borrowRecord)) {
                    System.out.println("Book \"" + bookName + "\" " + " successfully borrowed.");
                }
            }
        }
    }

    @Override
    public List<BorrowRecord> queryOnBorrow(String bookName, String author) {
        return queryOnBorrowRecord(bookName, author);
    }



    private boolean addBorrowRecord(Book book) {
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        try {
            // 获取根节点
            Node root = doc.getDocumentElement();
            // 创建新数据
            Element dataElement = doc.createElement("item");

            Element userNameElement = doc.createElement("userName");
            userNameElement.appendChild(doc.createTextNode(SessionManager.getInstance().getCurrentUser().getUserName()));
            dataElement.appendChild(userNameElement);

            Element bookNameElement = doc.createElement("bookName");
            bookNameElement.appendChild(doc.createTextNode(book.getBookName()));
            dataElement.appendChild(bookNameElement);

            Element authorElement = doc.createElement("author");
            authorElement.appendChild(doc.createTextNode(book.getAuthor()));
            dataElement.appendChild(authorElement);

            Element borrowDateElement = doc.createElement("borrowDate");
            borrowDateElement.appendChild(doc.createTextNode(String.valueOf(new Date())));
            dataElement.appendChild(borrowDateElement);

            Element isReturnedElement = doc.createElement("isReturned");
            isReturnedElement.appendChild(doc.createTextNode("no"));
            dataElement.appendChild(isReturnedElement);
            // 将新的数据追加到根节点下
            Node importedNode = doc.importNode(dataElement, true);
            root.appendChild(importedNode);

            // 将更新后的XML文档写回到文件中
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // 尝试格式化写入，实测有异常显示
//            transformer.setOutputProperty("indent", "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(BORROW_RECORDS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private List<BorrowRecord> queryOnBorrowRecord(String bookName, String author) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return borrowRecords;
        }
        BorrowRecord borrowRecord;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String userNameRecord = itemElement.getElementsByTagName("userName").item(0).getTextContent();
                String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                String authorRecord = itemElement.getElementsByTagName("author").item(0).getTextContent();
                String isReturnedRecord = itemElement.getElementsByTagName("isReturned").item(0).getTextContent();
                String borrowDateRecord = itemElement.getElementsByTagName("borrowDate").item(0).getTextContent();
                if (bookNameRecord.equals(bookName) && authorRecord.equals(author) && isReturnedRecord.equals("no")) {
                    borrowRecord = new BorrowRecord();
                    borrowRecord.setUserName(userNameRecord);
                    borrowRecord.setBookName(bookNameRecord);
                    borrowRecord.setIsReturned(isReturnedRecord);
                    borrowRecord.setBorrowDate(borrowDateRecord);
                    borrowRecords.add(borrowRecord);
                }
            }
        }
        return borrowRecords;
    }

    private BorrowRecord queryOneBorrowRecord(String bookName, String author, String userName) {
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        BorrowRecord borrowRecord;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String userNameRecord = itemElement.getElementsByTagName("userName").item(0).getTextContent();
                String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                String authorRecord = itemElement.getElementsByTagName("author").item(0).getTextContent();
                String isReturnedRecord = itemElement.getElementsByTagName("isReturned").item(0).getTextContent();
                String borrowDateRecord = itemElement.getElementsByTagName("borrowDate").item(0).getTextContent();
                if (bookNameRecord.equals(bookName) && authorRecord.equals(author)
                        && isReturnedRecord.equals("no") && userNameRecord.equals(userName)) {
                    borrowRecord = new BorrowRecord();
                    borrowRecord.setUserName(userNameRecord);
                    borrowRecord.setBookName(bookNameRecord);
                    borrowRecord.setIsReturned(isReturnedRecord);
                    borrowRecord.setBorrowDate(borrowDateRecord);
                    return borrowRecord;
                }
            }
        }
        return null;
    }

    private boolean updateBorrowRecord(BorrowRecord borrowRecord) {
        try {
            Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
            NodeList nodeList = doc.getElementsByTagName("item");
            if (nodeList.getLength() == 0) {
                return false;
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) node;
                    String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                    String userNameRecord = itemElement.getElementsByTagName("userName").item(0).getTextContent();
                    String isReturnedRecord = itemElement.getElementsByTagName("isReturned").item(0).getTextContent();
                    if (bookNameRecord.equals(borrowRecord.getBookName()) && userNameRecord.equals(borrowRecord.getUserName())
                            && isReturnedRecord.equals("no")) {
                        Element inventoryElement = (Element)itemElement.getElementsByTagName("isReturned").item(0);
                        inventoryElement.setTextContent("yes");
                        // 补充归还日期
                        Element returnDateElement = doc.createElement("returnDate");
                        returnDateElement.appendChild(doc.createTextNode(String.valueOf(new Date())));
                        itemElement.appendChild(returnDateElement);
                        break;
                    }
                }
            }
            // 将更新后的XML文档写回到文件中
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // 尝试格式化写入，实测有异常显示
//            transformer.setOutputProperty("indent", "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(BORROW_RECORDS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
