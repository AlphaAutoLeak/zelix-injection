package zelix.utils.system;

import java.net.*;
import java.io.*;

public class WebUtils
{
    public static String get(final String url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }
        in.close();
        return response.toString();
    }
}
