package com.hu.library.repository.impl;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import com.hu.library.repository.UserRepository;
import com.hu.library.server.XMLManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class XMLUserRepository implements UserRepository {

    private static final String USERS_FILE = "users_data.xml";
    /**
     * @param userName
     * @return
     */
    @Override
    public User queryUser(String userName) {
        Document doc = XMLManager.getDocument(USERS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                User item = itemParse(itemElement);
                if (item.getUserName().equals(userName)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * @param userType
     * @param userName
     * @param password
     * @return
     */
    @Override
    public boolean addUser(UserType userType, String userName, String password) {
        Document doc = XMLManager.getDocument(USERS_FILE);
        // 获取根节点
        Node root = doc.getDocumentElement();
        // 创建新数据
        Element dataElement = doc.createElement("item");

        Element userNameElement = doc.createElement("userName");
        userNameElement.appendChild(doc.createTextNode(userName));
        dataElement.appendChild(userNameElement);

        Element passwordElement = doc.createElement("password");
        passwordElement.appendChild(doc.createTextNode(password));
        dataElement.appendChild(passwordElement);

        Element userTypeElement = doc.createElement("userType");
        userTypeElement.appendChild(doc.createTextNode(userType.toString()));
        dataElement.appendChild(userTypeElement);
        // 将新的数据追加到根节点下
        Node importedNode = doc.importNode(dataElement, true);
        root.appendChild(importedNode);

        // 将更新后的XML文档写回到文件中
        XMLManager.writeDocument(doc, new File(USERS_FILE));
        return true;
    }

    private User itemParse(Element itemElement) {
        User item = new User();
        item.setUserName(itemElement.getElementsByTagName("userName").item(0).getTextContent());
        item.setPassword(itemElement.getElementsByTagName("password").item(0).getTextContent());
        item.setUserType(UserType.valueOf(itemElement.getElementsByTagName("userType").item(0).getTextContent()));
        return item;
    }
}
