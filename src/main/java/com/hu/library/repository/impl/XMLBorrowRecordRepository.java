package com.hu.library.repository.impl;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
import com.hu.library.repository.BorrowRecordRepository;
import com.hu.library.server.SessionManager;
import com.hu.library.server.XMLManager;
import com.hu.library.utils.DateUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLBorrowRecordRepository implements BorrowRecordRepository {

    private static final String BORROW_RECORDS_FILE = "borrow_records_data.xml";

    /**
     * @param book
     * @return
     */
    @Override
    public boolean addBorrowRecord(Book book) {
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
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
        borrowDateElement.appendChild(doc.createTextNode(DateUtils.formatCurrentDate()));
        dataElement.appendChild(borrowDateElement);

        Element returnDateElement = doc.createElement("returnDate");
        returnDateElement.appendChild(doc.createTextNode(""));
        dataElement.appendChild(returnDateElement);

        Element isReturnedElement = doc.createElement("isReturned");
        isReturnedElement.appendChild(doc.createTextNode("no"));
        dataElement.appendChild(isReturnedElement);
        // 将新的数据追加到根节点下
        Node importedNode = doc.importNode(dataElement, true);
        root.appendChild(importedNode);

        // 将更新后的XML文档写回到文件中
        XMLManager.writeDocument(doc, new File(BORROW_RECORDS_FILE));
        return true;
    }

    /**
     * @param bookName
     * @param author
     * @return
     */
    @Override
    public List<BorrowRecord> queryOnBorrowRecord(String bookName, String author) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return borrowRecords;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                BorrowRecord item = itemParse(itemElement);
                if (item.getBookName().equals(bookName) && item.getAuthor().equals(author)
                        && item.getIsReturned().equals("no")) {
                    borrowRecords.add(item);
                }
            }
        }
        return borrowRecords;
    }

    /**
     * @param bookName
     * @param author
     * @param userName
     * @return
     */
    @Override
    public BorrowRecord queryOneBorrowRecord(String bookName, String author, String userName) {
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                BorrowRecord item = itemParse(itemElement);
                if (item.getBookName().equals(bookName) && item.getAuthor().equals(author)
                        && item.getIsReturned().equals("no") && item.getUserName().equals(userName)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * @param borrowRecord
     * @return
     */
    @Override
    public boolean updateBorrowRecord(BorrowRecord borrowRecord) {
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                BorrowRecord item = itemParse(itemElement);
                if (item.getBookName().equals(borrowRecord.getBookName()) && item.getUserName().equals(borrowRecord.getUserName())
                        && item.getIsReturned().equals("no")) {
                    Element isReturnedElement = (Element)itemElement.getElementsByTagName("isReturned").item(0);
                    isReturnedElement.setTextContent("yes");
                    // 补充归还日期
                    Element returnDateElement = (Element)itemElement.getElementsByTagName("returnDate").item(0);
                    returnDateElement.setTextContent(DateUtils.formatCurrentDate());
                    break;
                }
            }
        }
        // 将更新后的XML文档写回到文件中
        XMLManager.writeDocument(doc, new File(BORROW_RECORDS_FILE));
        return true;
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public List<BorrowRecord> listBorrowedBooks(String userName) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        Document doc = XMLManager.getDocument(BORROW_RECORDS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return borrowRecords;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                BorrowRecord item = itemParse(itemElement);
                if (item.getIsReturned().equals("no") && item.getUserName().equals(userName)) {
                    borrowRecords.add(item);
                }
            }
        }
        return borrowRecords;
    }

    private BorrowRecord itemParse(Element itemElement) {
        BorrowRecord item = new BorrowRecord();
        item.setUserName(itemElement.getElementsByTagName("userName").item(0).getTextContent());
        item.setBookName(itemElement.getElementsByTagName("bookName").item(0).getTextContent());
        item.setAuthor(itemElement.getElementsByTagName("author").item(0).getTextContent());
        item.setIsReturned(itemElement.getElementsByTagName("isReturned").item(0).getTextContent());
        item.setBorrowDate(itemElement.getElementsByTagName("borrowDate").item(0).getTextContent());
        item.setReturnDate(itemElement.getElementsByTagName("returnDate").item(0).getTextContent());
        return item;
    }
}
