package GUI;
import LZW.*;
import LZW.Guard.GuardExecption;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class LZWGuardGUI {

    public static JTextField DnDField = new JTextField("Drop your file here");
    public static String FilePath = "";
    public static JLabel loading = new JLabel();

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        JFrame mainJFrame= new JFrame("Guard LZW");

        loading.setBounds(350,200,150,100);

        JPasswordField GuardPassword = new JPasswordField(16);
        PlainDocument document = (PlainDocument) GuardPassword.getDocument();
        //source https://www.tutorialspoint.com/how-to-restrict-the-number-of-digits-inside-jpasswordfield-in-java
        document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String passwordCurrentString = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (passwordCurrentString.length() <= 16) {
                    super.replace(fb, offset, length, text, attrs);
                }
                if(passwordCurrentString.length()==0){
                    passwordCurrentString = "1234567891011121";
                }
                Runner.setPassword(passwordCurrentString);
            }
        });
        Image guardIcon = Toolkit.getDefaultToolkit().getImage("GUI/Icon.png");
        GuardPassword.setVisible(false);
        JLabel label = new JLabel("\uD83D\uDEE1 Guard™");
        JCheckBox checkbox = new JCheckBox();
        checkbox.addActionListener(event -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                GuardPassword.setVisible(true);
                Runner.setActivateGuard(true);
            } else {
                GuardPassword.setVisible(false);
                Runner.setActivateGuard(false);
            }
        });
        checkbox.setToolTipText("This will allow you to choose a password\n and your compressed files will be encrypted using AES 128-bit Encryption.");

        JButton compressButton=new JButton("Compress");
        compressButton.setBounds(200,200,120,30);
        compressButton.addActionListener(e -> {
            try {
                Runner.RunCompress();
            } catch (GuardExecption guardExecption) {
                guardExecption.printStackTrace();
            }
        });
        compressButton.addActionListener(e -> DnDField.setText("Drop your file here"));
        compressButton.addActionListener(e ->FilePath = "");
        JButton DecompressButton=new JButton("Decompress");
        DecompressButton.setBounds(580,200,120,30);
        DecompressButton.addActionListener(e -> {
            try {
                Runner.RunDecompress();
            } catch (GuardExecption guardExecption) {
                guardExecption.printStackTrace();
            }
        });
        DecompressButton.addActionListener(e ->DnDField.setText("Drop your file here"));
        DecompressButton.addActionListener(e ->FilePath = "");

        DnDField.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        FilePath = FilePath + " ➕ " + file.getPath();
                        Runner.inputSourceQueue.add(file.getPath());
                        Runner.FileQueue.add(file.getName());
                    }
                    DnDField.setText(FilePath);
                    evt.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenu main = new JMenu("GitHub");
        JMenuBar mainBar = new JMenuBar();
        JMenuItem github1 = new JMenuItem(new AbstractAction("Kogwork") {
            public void actionPerformed(ActionEvent e) {
                try {

                    Desktop.getDesktop().browse(new URI("https://github.com/Kogwork"));

                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem github2 = new JMenuItem(new AbstractAction("Lempobot") {
            public void actionPerformed(ActionEvent e) {
                try {

                    Desktop.getDesktop().browse(new URI("https://github.com/Lempobot"));

                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        main.add(github1);
        main.add(github2);
        mainBar.add(main);
        mainJFrame.setJMenuBar(mainBar);
        GuardPassword.setBounds(200,110, 200,40);
        DnDField.setBounds(100,50, 700,50);
        label.setBounds(120,110, 100,40);
        checkbox.setBounds(100,120, 20,20);
        mainJFrame.setIconImage(guardIcon);
        mainJFrame.add(DnDField); mainJFrame.add(GuardPassword); mainJFrame.add(mainBar); mainJFrame.add(checkbox); mainJFrame.add(label);
        mainJFrame.add(compressButton); mainJFrame.add(DecompressButton); mainJFrame.add(loading);
        mainJFrame.setSize(1000,600);
        mainJFrame.setLayout(null);
        mainJFrame.setVisible(true);

    }

}
