package zelix.utils;

import java.io.*;
import java.util.*;

public class FileUtils
{
    public static String readFile(final String path) {
        final StringBuilder result = new StringBuilder();
        try {
            final File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            final FileInputStream fIn = new FileInputStream(file);
            try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn))) {
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    result.append(str);
                    result.append(System.lineSeparator());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    public static String readFile(final File file) {
        final StringBuilder result = new StringBuilder();
        try {
            final FileInputStream fIn = new FileInputStream(file);
            try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn))) {
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    result.append(str);
                    result.append(System.lineSeparator());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    public static List<File> getFilesFromDir(final String dir) {
        final List<File> file = new ArrayList<File>();
        final File startFolder = new File(dir);
        final File[] listFiles;
        final File[] files = listFiles = startFolder.listFiles();
        for (final File tempFile : listFiles) {
            if (startFolder.isDirectory()) {
                file.addAll(getFilesFromDir(tempFile.getAbsolutePath()));
            }
            else {
                file.add(tempFile);
            }
        }
        return file;
    }
}
