package decisionlist.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JButtonMain extends JButton {

    public JButtonMain(String text, byte type) {
        super(text);
        switch (type) {
            case 1 :
                initializeButtonTypeOne();
                break;
            case 2 :
                initializeButtonTypeTwo();
                break;
        }
    }

    private void initializeButtonTypeTwo() {
        setFont(new Font("Courier New", Font.BOLD, 13));
        setPreferredSize(new Dimension(130,40));
        setBorderPainted(false);
        setFocusPainted(false);
        setBackground(new Color(87,88,90));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initializeButtonTypeOne() {
        setFont(new Font("Courier New", Font.BOLD, 13));
        setPreferredSize(new Dimension(200,40));
        setBorderPainted(false);
        setFocusPainted(false);
        setBackground(new Color(249,186,72));
        setForeground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

}