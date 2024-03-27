package com.hu.library.service.impl;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
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
import java.util.List;

public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE = "books_data.xml";

    @Override
    public void addBook(String bookName, String author, int inventory) {
        Book book = queryBook(bookName);
        if (book == null) {
            // 用户不存在，新增用户
            if (addOneBook(bookName, author, inventory)) {
                System.out.println("Book" + " \"" + bookName + "\" by" + author + " added successfully, inventory:" + inventory);
            }
        } else {
            int newInventory = inventory + book.getInventory();
            if (updateBook(bookName, newInventory)) {
                System.out.println("Book" + " \"" + bookName + "\" by" + author + " inventory successfully updated, new inventory:" + newInventory);
            }
        }
    }


    @Override
    public void deleteBook(String bookName, String author) {
        BorrowRecordService borrowRecordService = new BorrowRecordServiceImpl();
        List<BorrowRecord> borrowRecords = borrowRecordService.queryOnBorrow(bookName, author);
        if (borrowRecords.size() > 0) {
            System.out.println("Cannot delete book \"" + bookName + "\" because it is currently borrowed.");
        } else {
            if (deleteOneBook(bookName, author)) {
                System.out.println("Book \"" + bookName + "\" delete successfully.");
            }
        }
    }

    @Override
    public void listBooks() {
        System.out.println("Book List:");
        List<Book> books = queryAllBooks();
        if (!books.isEmpty()) {
            for (Book book : books) {
                System.out.println(book.getBookName() + " - " + book.getAuthor() + " - Inventory：" + book.getInventory());
            }
        }
    }

    @Override
    public Book search(String bookName, String author) {
        Book book = queryBookByNameAuthor(bookName, author);
        if (book != null) {
            System.out.println(book.getBookName() + " - " + book.getAuthor() + " - Inventory：" + book.getInventory());
        }
        return book;
    }

    private Book queryBook(String bookName) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        Book book = new Book();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String name = itemElement.getElementsByTagName("name").item(0).getTextContent();
                String author = itemElement.getElementsByTagName("author").item(0).getTextContent();
                String inventory = itemElement.getElementsByTagName("inventory").item(0).getTextContent();
                if (name.equals(bookName)) {
                    book.setBookName(name);
                    book.setAuthor(author);
                    book.setInventory(Integer.parseInt(inventory));
                    return book;
                }
            }
        }
        return null;
    }

    public Book queryBookByNameAuthor(String bookName, String author) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        Book book;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                String authorRecord = itemElement.getElementsByTagName("author").item(0).getTextContent();
                String inventory = itemElement.getElementsByTagName("inventory").item(0).getTextContent();
                if (bookNameRecord.equals(bookName) && authorRecord.equals(author)) {
                    book = new Book();
                    book.setBookName(bookNameRecord);
                    book.setAuthor(authorRecord);
                    book.setInventory(Integer.parseInt(inventory));
                    return book;
                }
            }
        }
        return null;
    }

    private List<Book> queryAllBooks() {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        Book book;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                String author = itemElement.getElementsByTagName("author").item(0).getTextContent();
                int inventory = Integer.parseInt(itemElement.getElementsByTagName("inventory").item(0).getTextContent());
                if (inventory > 0) {
                    book = new Book();
                    book.setBookName(bookNameRecord);
                    book.setAuthor(author);
                    book.setInventory(inventory);
                    books.add(book);
                }
            }
        }
        return books;
    }

    public boolean addOneBook(String bookName, String author, int inventory) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        try {
            // 获取根节点
            Node root = doc.getDocumentElement();
            // 创建新数据
            Element dataElement = doc.createElement("item");

            Element bookNameElement = doc.createElement("bookName");
            bookNameElement.appendChild(doc.createTextNode(bookName));
            dataElement.appendChild(bookNameElement);

            Element authorElement = doc.createElement("author");
            authorElement.appendChild(doc.createTextNode(author));
            dataElement.appendChild(authorElement);

            Element inventoryElement = doc.createElement("inventory");
            inventoryElement.appendChild(doc.createTextNode(String.valueOf(inventory)));
            dataElement.appendChild(inventoryElement);
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
            StreamResult result = new StreamResult(new File(BOOKS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean updateBook(String bookName, int inventory) {
        try {
            Document doc = XMLManager.getDocument(BOOKS_FILE);
            NodeList nodeList = doc.getElementsByTagName("item");
            if (nodeList.getLength() == 0) {
                return false;
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) node;
                    String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                    if (bookNameRecord.equals(bookName)) {
                        Element inventoryElement = (Element)itemElement.getElementsByTagName("inventory").item(0);
                        inventoryElement.setTextContent(String.valueOf(inventory));
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
            StreamResult result = new StreamResult(new File(BOOKS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private boolean deleteOneBook(String bookName, String author) {
        try {
            Document doc = XMLManager.getDocument(BOOKS_FILE);
            NodeList nodeList = doc.getElementsByTagName("item");
            if (nodeList.getLength() == 0) {
                return false;
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) node;
                    String bookNameRecord = itemElement.getElementsByTagName("bookName").item(0).getTextContent();
                    String authorRecord = itemElement.getElementsByTagName("author").item(0).getTextContent();
                    if (bookNameRecord.equals(bookName) && authorRecord.equals(author)) {
                        // 找到匹配的节点，从父节点中移除
                        Node parent = itemElement.getParentNode();
                        parent.removeChild(itemElement);
                        break; // 删除完毕后直接退出循环
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
            StreamResult result = new StreamResult(new File(BOOKS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
