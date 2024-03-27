package com.hu.library.service.impl;

import com.hu.library.entity.User;
import com.hu.library.enums.UserType;
import com.hu.library.server.SessionManager;
import com.hu.library.server.XMLManager;
import com.hu.library.service.UserService;
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

public class UserServiceImpl implements UserService {

    private static final String USERS_FILE = "users_data.xml";



    @Override
    public void register(UserType userType, String userName, String password) {
//        System.out.println("user register!");
        // 用户存在，直接返回提示
        if (queryUser(userName) != null) {
            System.out.println("user already exists!");
            return;
        }
        // 用户不存在，新增用户
        if (addUser(userType, userName, password)) {
            System.out.println(userType + " " + userName + " successfully registered.");
        }
    }



    @Override
    public void login(String userName, String password) {
        User user = queryUser(userName);
        if (user == null) {
            System.out.println("user not exist!");
            return;
        }
        if (user.getPassword().equals(password)) {
            SessionManager.getInstance().setCurrentUser(user);
            System.out.println(user.getUserType() + " " + userName + " successfully logged in.");
        } else {
            System.out.println("The password is incorrect");
        }
    }

    @Override
    public void remove(String userName) {

    }

    /**
     * 判断用户是否存在
     * @param userName
     * @return
     */

    private User queryUser(String userName) {
        Document doc = XMLManager.getDocument(USERS_FILE);
        NodeList nodeList = doc.getElementsByTagName("item");
        if (nodeList.getLength() == 0) {
            return null;
        }
        User user = new User();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) node;
                String userNameExist = itemElement.getElementsByTagName("userName").item(0).getTextContent();
                String password = itemElement.getElementsByTagName("password").item(0).getTextContent();
                String userType = itemElement.getElementsByTagName("userType").item(0).getTextContent();
                if (userNameExist.equals(userName)) {
                    user.setUserName(userName);
                    user.setPassword(password);
                    user.setUserType(UserType.valueOf(userType));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * 新增user，数据保存到xml中
     * @param userType
     * @param userName
     * @param password
     */
    private boolean addUser(UserType userType, String userName, String password) {
        Document doc = XMLManager.getDocument(USERS_FILE);
        try {
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
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // 尝试格式化写入，实测有异常显示
//            transformer.setOutputProperty("indent", "yes");
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(USERS_FILE));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;

    }
}
