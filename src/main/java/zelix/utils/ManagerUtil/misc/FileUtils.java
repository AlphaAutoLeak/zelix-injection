package zelix.utils.ManagerUtil.misc;

import java.io.*;

public class FileUtils
{
    public static String readFile(final File file) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    public static String readInputStream(final InputStream inputStream) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
