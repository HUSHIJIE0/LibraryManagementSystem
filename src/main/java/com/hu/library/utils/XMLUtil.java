package com.hu.library.utils;

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

/**
 * XML 管理类
 * 获取xml文档记录的DOM数据，初始化XML文件，XML数据写入等操作
 */
public class XMLUtil {

    /**
     * 根据fileName获取对应文件的Document，包含文件的处理
     * @param fileName
     * @return
     */
    public static Document getDocument(String fileName){
        // 获取文件，文件不存在，则创建文件并完成xml的初始化
        File file = getFileByName(fileName);
        return getDocument(file);
    }

    /**
     * 根据file获取对应文件的Document
     * @param file
     * @return
     */
    public static Document getDocument(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            throw new RuntimeException("Error loading XML document", e);
        }
    }

    /**
     * 从磁盘读取文件，文件不存在则创建，并初始化
     * @param fileName
     * @return
     */
    private static File getFileByName(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
//                System.out.println("文件 " + fileName + " 存在");
            } else {
                if (file.createNewFile()) {
                    // 初始化为xml格式文件
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

    /**
     * 初始化文件，写入根节点
     * @param file
     */
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

    /**
     * xml文档内容更新写入
     * @param document
     * @param file
     */
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

