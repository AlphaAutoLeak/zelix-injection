package zelix.command;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;
import zelix.utils.hooks.visual.*;

public class Esu extends Command
{
    public Esu() {
        super("Esu");
    }
    
    public static String getJsonData(final String jsonString, final String targetString) {
        final JsonParser parser = new JsonParser();
        final JsonElement jsonNode = parser.parse(jsonString);
        if (jsonNode.isJsonObject()) {
            final JsonObject jsonObject = jsonNode.getAsJsonObject();
            final JsonElement jsonElementId = jsonObject.get(targetString);
            return jsonElementId.toString();
        }
        return null;
    }
    
    public static String deleteCharInString(final String str, final char delChar) {
        final StringBuilder delStr = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) != delChar) {
                delStr.append(str.charAt(i));
            }
        }
        return delStr.toString();
    }
    
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
    
    public static String sendGet(final String url, final String param) {
        String result = "";
        BufferedReader in = null;
        try {
            final URL realUrl = new URL(url);
            final URLConnection connection = realUrl.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(99781);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            final Map<String, List<String>> map = connection.getHeaderFields();
            for (String s : map.keySet()) {}
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = String.valueOf(result) + line;
            }
        }
        catch (Exception exception) {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception ex) {}
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception ex2) {}
        }
        return result;
    }
    
    @Override
    public void runCommand(final String s, final String[] args) {
        try {
            if (args.length == 0) {
                ChatUtils.error("Usage: " + this.getSyntax());
            }
            String jsonData = sendGet("https://qb-api.ltd/allcha.php?qq=" + args[0], null);
            jsonData = deleteCharInString(jsonData, '\n');
            jsonData = deleteCharInString(jsonData, '\r');
            jsonData = deleteCharInString(jsonData, ' ');
            try {
                final String parsedData = getJsonData(jsonData, "data");
                final String qq = getJsonData(parsedData, "qq");
                final String mobile = getJsonData(parsedData, "mobile");
                ChatUtils.success("QQ:" + qq + ",mobile:" + mobile);
            }
            catch (Exception e) {
                ChatUtils.message("Can't Find!");
            }
        }
        catch (Exception e2) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }
    
    @Override
    public String getDescription() {
        return "Esu";
    }
    
    @Override
    public String getSyntax() {
        return "esu <QQNumber>";
    }
}
