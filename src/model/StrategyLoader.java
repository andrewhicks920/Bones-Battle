package model;

import java.io.File;
import java.io.FileInputStream;

public class StrategyLoader extends ClassLoader {
    public StrategyLoader() {}

    private byte[] readClass(String className) {
        try {
            File file = new File(className + ".class");
            FileInputStream fis = new FileInputStream(file);
            byte[] classBytes = new byte[(int) file.length()];
            fis.read(classBytes);
            return classBytes;
        }
        catch (Exception e) {
            System.out.println("ERROR:  Strategy " + className + " could not be read from current directory.\n  Exception is: " + e + "\n  Message is: " + e.getMessage() + "\n  Stack dump follows.\n");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public Class loadClass(String className) throws ClassNotFoundException, ClassFormatError {
        return this.loadClass(className, true);
    }

    public Class loadClass(String className, boolean resolve) throws ClassNotFoundException, ClassFormatError {
        try {
            return this.findSystemClass(className);
        }
        catch (ClassNotFoundException e) {
            byte[] classData = this.readClass(className);
            if (classData == null) {
                throw new ClassNotFoundException();
            }
            else {
                Class loadedClass = this.defineClass((String) null, classData, 0, classData.length);
                if (loadedClass == null)
                    throw new ClassFormatError();
                else {
                    if (resolve) this.resolveClass(loadedClass);
                    return loadedClass;
                }
            }
        }
    }
}
