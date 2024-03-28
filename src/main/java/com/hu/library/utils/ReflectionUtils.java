package com.hu.library.utils;

import com.hu.library.command.CommandHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类
 */
public class ReflectionUtils {
    /**
     * 获取实现了指定接口的所有类
     * @param interfaceClass
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getClassesImplementing(Class<?> interfaceClass) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        // 获取接口类包所在地址
        String packageName = interfaceClass.getPackageName();
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 类加载器加载资源
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            // 判断资源类型，如果是文件类型，可以直接用目录的方式读取，如果是jar包，则采用jar包内文件遍历方式读取
            if (resource.getProtocol().equals("file")) {
                File file = new File(resource.getFile());
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName));
                }
            } else {
                // 处理JAR文件中的类
                String jarPath = resource.getFile().split("!")[0].replace("file:", "");
                loadClassesFromJarFile(jarPath, classes);
            }
        }
        return classes;
    }

    /**
     * 查询文件系统目录下的接口实现
     * @param directory
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    private static Set<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> cls = Class.forName(className);
                if (isImplementingInterface(cls)) {
                    classes.add(cls);
                }
            }
        }
        return classes;
    }

    private static boolean isImplementingInterface(Class<?> cls) {
        for (Class<?> intf : cls.getInterfaces()) {
            if (intf.equals(CommandHandler.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询jar包中的接口实现
     * @param jarPath
     * @param implementations
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void loadClassesFromJarFile(String jarPath, Set<Class<?>> implementations) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().replace("/", ".").replace(".class", "");
                Class<?> clazz = Class.forName(className);
                if (CommandHandler.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                    implementations.add(clazz);
                }
            }
        }
        jarFile.close();
    }
}


