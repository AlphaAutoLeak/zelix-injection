package zelix.utils.frame;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.regex.*;
import java.net.*;
import java.io.*;

public class GET extends JFrame
{
    private JPanel contentPane;
    private JTextField qqname;
    private String QQ;
    private String bt;
    private String OKQQ;
    private String Sjgsd;
    private JLabel lblNewLabel_1;
    private JLabel qqokname;
    
    public GET() {
        this.bt = "\u67e5Q\u7ed1-<\ufffd\u0536\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd>";
        this.setResizable(false);
        this.setTitle(this.bt);
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 495, 305);
        (this.contentPane = new JPanel()).setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(null);
        final JLabel lblNewLabel = new JLabel("\u8f93\u5165\u4f60\u8981\u67e5\u8be2\u7684QQ\u53f7\uff1a");
        lblNewLabel.setFont(new Font("\u03a2\ufffd\ufffd\ufffd\u017a\ufffd", 1, 18));
        lblNewLabel.setBounds(10, 26, 236, 54);
        this.contentPane.add(lblNewLabel);
        (this.qqname = new JTextField()).setHorizontalAlignment(2);
        this.qqname.setFont(new Font("\ufffd\ufffd\ufffd\ufffd", 1, 20));
        this.qqname.setToolTipText("\u8bf7\u8f93\u5165\u6570\u5b57");
        this.qqname.setBounds(48, 81, 378, 38);
        this.contentPane.add(this.qqname);
        this.qqname.setColumns(10);
        (this.lblNewLabel_1 = new JLabel("")).setFont(new Font("\ufffd\ufffd\ufffd\ufffd", 1, 17));
        this.lblNewLabel_1.setBounds(116, 129, 236, 38);
        this.contentPane.add(this.lblNewLabel_1);
        final JButton btnNewButton = new JButton("\u5f00\u59cb\u67e5\u8be2");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (GET.isInteger(GET.this.qqname.getText())) {
                    GET.this.setTitle(GET.this.bt + " \ufffd\ufffd\ufffd\ufffd\u016c\ufffd\ufffd\ufffd\u0132\ufffd\u046f\ufffd\u0423\ufffd\ufffd\ufffd\ufffd\u053a\udb46\udd21\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd");
                    GET.this.QQ = GET.this.qqname.getText();
                    try {
                        GET.this.OKQQ = get("http://api.cjsrcw.cn/qb-api.php?mod=cha&qq=" + GET.this.QQ);
                        if (GET.this.OKQQ != null) {
                            final String OK = GET.this.OKQQ.substring(GET.this.OKQQ.indexOf("\"mobile\"") + 11, GET.this.OKQQ.indexOf("\"mobile\"") + 22);
                            if (GET.isInteger(OK)) {
                                GET.this.setTitle(GET.this.bt + " \ufffd\ufffd\u046f\ufffd\u0279\ufffd,\ufffd\ufffd\u03aa\ufffd\ufffd\ufffd\u0536\ufffd\ufffd\ufffd\ufffd\u01a3\ufffd");
                                GET.this.lblNewLabel_1.setText("\ufffd\ufffd\u046f\ufffd\u0279\ufffd,\ufffd\ufffd\u03aa\ufffd\ufffd\ufffd\u0536\ufffd\ufffd\ufffd\ufffd\u01a3\ufffd");
                                final String gui = get("https://www.sogou.com/websearch/phoneAddress.jsp?phoneNumber=" + OK + "&cb=djsonp1606391530084");
                                final String guishu = gui.substring(gui.indexOf("(") + 2, gui.indexOf(")") - 1);
                                GET.this.qqokname.setText("\ufffd\u05bb\ufffd\ufffd\u0163\ufffd" + OK + " \ufffd\u05bb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0623\ufffd" + guishu);
                                GET.this.copy("QQ\ufffd\u0163\ufffd" + GET.this.QQ + " \ufffd\u05bb\ufffd\ufffd\u0163\ufffd" + OK + " \ufffd\u05bb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0623\ufffd" + guishu);
                            }
                            else {
                                GET.this.lblNewLabel_1.setText("\ufffd\ufffd\u046f\u02a7\ufffd\ufffd,\ufffd\ufffd\ufffd\u0432\ufffd\u00fb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd?");
                                GET.this.setTitle(GET.this.bt + " \ufffd\ufffd\u046f\u02a7\ufffd\ufffd,\ufffd\ufffd\ufffd\u0432\ufffd\u00fb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd?");
                            }
                        }
                        else {
                            GET.this.lblNewLabel_1.setText("\ufffd\ufffd\u046f\ufffd\u04ff\ufffd\ufffd\ufffd\u02a7\u0427,\ufffd\ufffd\ufffd\ufffd\ufffd\u0531\ufffd\ufffd\ufffd?\ufffd");
                        }
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                else {
                    GET.this.lblNewLabel_1.setText("\u8bf7\u8f93\u5165\u6b63\u786e\u7684QQ\u53f7\u7801\uff01");
                }
            }
        });
        btnNewButton.setFont(new Font("\u03a2\ufffd\ufffd\ufffd\u017a\ufffd Light", 1, 18));
        btnNewButton.setToolTipText("\u5f00\u59cb\u67e5\u8be2");
        btnNewButton.setBounds(90, 206, 296, 45);
        this.contentPane.add(btnNewButton);
        (this.qqokname = new JLabel("")).setFont(new Font("\ufffd\ufffd\ufffd\ufffd", 1, 17));
        this.qqokname.setBounds(10, 177, 469, 25);
        this.contentPane.add(this.qqokname);
    }
    
    public void copy(final String str) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), null);
    }
    
    public static boolean isInteger(final String str) {
        final Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    
    private static String get(final String url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
