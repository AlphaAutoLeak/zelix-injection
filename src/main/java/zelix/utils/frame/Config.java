package zelix.utils.frame;

import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import zelix.utils.system.*;
import zelix.utils.*;
import javax.swing.*;
import java.io.*;
import com.google.gson.*;
import zelix.managers.*;
import java.util.*;
import zelix.hack.*;
import zelix.value.*;

public class Config extends JFrame
{
    public static String Cloudurl;
    public static String ReturnData;
    private static JsonParser jsonParser;
    private JPanel contentPane;
    
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final Config frame = new Config();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public Config() {
        this.setTitle("Cloud-Config-Loader");
        this.setBounds(100, 100, 450, 300);
        (this.contentPane = new JPanel()).setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(null);
        final JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("Fill With Config-Author");
        textArea.setBounds(0, 10, 434, 24);
        this.contentPane.add(textArea);
        final JTextArea textArea2 = new JTextArea();
        textArea2.setBackground(Color.WHITE);
        textArea2.setToolTipText("Fill With Config-Name");
        textArea2.setBounds(0, 40, 434, 24);
        this.contentPane.add(textArea2);
        final JButton btnNewButton = new JButton("Load");
        btnNewButton.setBounds(10, 74, 103, 25);
        this.contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Config.Cloudurl = "http://checker.dpro.site/" + textArea.getText() + "/HWID.txt";
                    Config.ReturnData = WebUtils.get(Config.Cloudurl);
                    if (!Config.ReturnData.contains(Utils.getHWID(false))) {
                        JOptionPane.showMessageDialog(null, "Did Not Bind Your HWID");
                        return;
                    }
                    Config.Cloudurl = "http://checker.dpro.site/" + textArea.getText() + "/" + textArea2.getText() + ".json";
                    Config.loadHacks(Config.ReturnData = WebUtils.get(Config.Cloudurl));
                }
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Null Point");
                }
            }
        });
    }
    
    public static void loadHacks(final String parse) {
        JsonObject jsonObject = null;
        try {
            jsonObject = (JsonObject)Config.jsonParser.parse(parse);
            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final Hack hack = HackManager.getHack(entry.getKey());
                if (hack == null) {
                    continue;
                }
                final JsonObject jsonObjectHack = (JsonObject)entry.getValue();
                hack.setKey(jsonObjectHack.get("key").getAsInt());
                hack.setToggled(jsonObjectHack.get("toggled").getAsBoolean());
                if (hack.getValues().isEmpty()) {
                    continue;
                }
                for (final Value value : hack.getValues()) {
                    if (value instanceof BooleanValue) {
                        value.setValue(jsonObjectHack.get(value.getName()).getAsBoolean());
                    }
                    if (value instanceof NumberValue) {
                        value.setValue(jsonObjectHack.get(value.getName()).getAsDouble());
                    }
                    if (value instanceof ModeValue) {
                        final ModeValue modeValue = (ModeValue)value;
                        for (final Mode mode : modeValue.getModes()) {
                            mode.setToggled(jsonObjectHack.get(mode.getName()).getAsBoolean());
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        Config.jsonParser = new JsonParser();
    }
}
