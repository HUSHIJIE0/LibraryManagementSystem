package com.hu.library.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader {
    JarFile jarFile;

    public JarClassLoader(File jarFile) throws IOException {
        this.jarFile = new JarFile(jarFile);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String className = name.replace('.', File.separatorChar) + ".class";
            JarEntry entry = jarFile.getJarEntry(className);
            if (entry != null) {
                byte[] classData = getClassData(entry);
                return defineClass(name, classData, 0, classData.length);
            } else {
                throw new ClassNotFoundException(name);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    private byte[] getClassData(JarEntry entry) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = jarFile.getInputStream(entry)) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return out.toByteArray();
    }
}
