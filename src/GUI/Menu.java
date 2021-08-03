package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Menu {
    Menu() {
        JMenu main = new JMenu("GitHub");
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
    }

}
