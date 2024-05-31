package com.poly.utils;

import java.nio.file.Paths;

public class PathUtils {
    public static String getProjectRootPath() {
        try {
            return Paths.get("")
                    .toAbsolutePath()
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
