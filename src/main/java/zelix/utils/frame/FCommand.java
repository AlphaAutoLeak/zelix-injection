package zelix.utils.frame;

import javax.swing.border.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import zelix.managers.*;
import zelix.utils.hooks.visual.*;

public class FCommand extends JFrame
{
    private JPanel contentPane;
    
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final FCommand frame = new FCommand();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public FCommand() {
        this.setTitle("Command Frame Fix By Zenwix");
        this.setBounds(100, 100, 450, 300);
        (this.contentPane = new JPanel()).setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout(null);
        final JTextArea textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setToolTipText("Fill in commands");
        textArea.setBounds(0, 39, 434, 24);
        this.contentPane.add(textArea);
        final JButton btnNewButton = new JButton("Run");
        btnNewButton.setBounds(10, 74, 103, 25);
        this.contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    final String message = textArea.getText();
                    CommandManager.getInstance().runCommands("." + message);
                    ChatUtils.message("run...");
                }
                catch (Exception var5) {
                    var5.printStackTrace();
                }
            }
        });
    }
}
