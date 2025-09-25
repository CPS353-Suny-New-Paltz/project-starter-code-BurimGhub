package project.checkpointtests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Class<?>> loadAllClasses() throws Exception {
        File rootDir = new File("src");
        List<Class<?>> result = new ArrayList<>();
        for (File f : rootDir.listFiles()) {
            loadClassesRec(f, result, "");
        }
        return result;
    }
    
    public static List<Class<?>> loadAllTestClasses() throws Exception {
        File rootDir = new File("test");
        List<Class<?>> result = new ArrayList<>();
        for (File f : rootDir.listFiles()) {
            loadClassesRec(f, result, "");
        }
        return result;
    }

    private static void loadClassesRec(File f, List<Class<?>> result, String packageName) throws Exception {
        if (f.isDirectory()) {
            if (!packageName.isEmpty()) {
                packageName += ".";
            }
            packageName += f.getName();
            for (File child : f.listFiles()) {
                loadClassesRec(child, result, packageName);
            }
        } else if (f.getName().endsWith(".java")) {
            String className = f.getName().substring(0, f.getName().length() - 5);
            String fullName = packageName + (packageName.isEmpty() ? "" : ".") + className;
            result.add(Utils.class.getClassLoader().loadClass(fullName));
        }
    }
}
