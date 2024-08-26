package org.bayaweaver.domainmodel.util;

public class JavaClassNameUtil {

    private JavaClassNameUtil() {}

    public static String toHumanReadableName(Class<?> c) {
        StringBuilder result = new StringBuilder();
        String className = c.getSimpleName();
        for (int i = 0; i < className.length(); i++) {
            char currentChar = className.charAt(i);
            if (Character.isUpperCase(currentChar) && i != 0) {
                result.append(' ');
            }
            result.append(currentChar);
        }
        return result.toString();
    }
}
