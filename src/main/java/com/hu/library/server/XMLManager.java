package com.hu.library.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

// XML 管理类
public class XMLManager {

    public static Document getDocument(String fileName){
        File file = getFileByName(fileName);
        return getDocument(file);
    }

    public static Document getDocument(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            throw new RuntimeException("Error loading XML document", e);
        }
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
            writeDocument(doc, file);

//            System.out.println("文件初始化完成" + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDocument(Document document, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException("Error writing XML document", e);
        }
    }

}

