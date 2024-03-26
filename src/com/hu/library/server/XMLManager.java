package com.hu.library.server;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.hu.library.entity.Book;
import com.hu.library.entity.BorrowRecord;
import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

// XML 管理类
public class XMLManager {

    public static Document getDocument(String fileName){
        File file = getFileByName(fileName);
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;

    }

    private static File getFileByName(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
//                System.out.println("文件 " + fileName + " 存在");
            } else {
                if (file.createNewFile()) {
                    initFile(file);
//                    System.out.println("文件 " + fileName + " 已创建");
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initFile(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // 创建根元素
            Element rootElement = doc.createElement("items");
            doc.appendChild(rootElement);

            // 将文档写入XML文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

//            System.out.println("文件初始化完成" + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

