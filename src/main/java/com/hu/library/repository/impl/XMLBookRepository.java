package com.hu.library.repository.impl;

import com.hu.library.entity.Book;
import com.hu.library.repository.BookRepository;
import com.hu.library.server.XMLManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLBookRepository implements BookRepository {

    private static final String BOOKS_FILE = "books_data.xml";

    /**
     * @param bookName
     * @return
     */
    @Override
    public Book queryBook(String bookName) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                Book item = itemParse(itemElement);
                if (item.getBookName().equals(bookName)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * @param bookName
     * @param author
     * @return
     */
    @Override
    public Book queryBookByNameAuthor(String bookName, String author) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                Book item = itemParse(itemElement);
                if (item.getBookName().equals(bookName) && item.getAuthor().equals(author)) {
                    return item;
                }
            }
        }
        return null;
    }


    /**
     * @return
     */
    @Override
    public List<Book> queryAllBooks() {
        List<Book> books = new ArrayList<>();
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return books;
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                Book book = itemParse(itemElement);
                if (book.getInventory() > 0) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    /**
     * @param bookName
     * @param author
     * @param inventory
     * @return
     */
    @Override
    public boolean addOneBook(String bookName, String author, int inventory) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
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
        XMLManager.writeDocument(doc, new File(BOOKS_FILE));
        return true;
    }

    /**
     * @param bookName
     * @param inventory
     * @return
     */
    @Override
    public boolean updateBook(String bookName, int inventory) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                Book item = itemParse(itemElement);
                if (item.getBookName().equals(bookName)) {
                    Element inventoryElement = (Element)itemElement.getElementsByTagName("inventory").item(0);
                    inventoryElement.setTextContent(String.valueOf(inventory));
                    break;
                }

            }
        }
        // 将更新后的XML文档写回到文件中
        XMLManager.writeDocument(doc, new File(BOOKS_FILE));
        return true;
    }

    /**
     * @param bookName
     * @param author
     * @return
     */
    @Override
    public boolean deleteOneBook(String bookName, String author) {
        Document doc = XMLManager.getDocument(BOOKS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return false;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                Book item = itemParse(itemElement);
                if (item.getBookName().equals(bookName) && item.getAuthor().equals(author)) {
                    // 找到匹配的节点，从父节点中移除
                    Node parent = itemElement.getParentNode();
                    parent.removeChild(itemElement);
                    break; // 删除完毕后直接退出循环
                }
            }
        }
        // 将更新后的XML文档写回到文件中
        XMLManager.writeDocument(doc, new File(BOOKS_FILE));
        return true;
    }

    private Book itemParse(Element itemElement) {
        Book item = new Book();
        item.setBookName(itemElement.getElementsByTagName("bookName").item(0).getTextContent());
        item.setAuthor(itemElement.getElementsByTagName("author").item(0).getTextContent());
        item.setInventory(Integer.parseInt(itemElement.getElementsByTagName("inventory").item(0).getTextContent()));
        return item;
    }
}
