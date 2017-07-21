package decisionlist.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JLabelMain extends JLabel {

    public JLabelMain(String text, byte type) {
        super(text);
        switch (type) {
            case 1 :
                labelTypeOne();
                break;
            case 2 :
                labelTypeTwo();
                break;
            case 3 :
                labelTypeThree();
                break;
        }
    }

    private void labelTypeThree() {
        setFont(new Font("Courier New", Font.BOLD, 12));
        setForeground(new Color(249,186,72));
        setPreferredSize(new Dimension(700,30));
        setHorizontalAlignment(LEADING);
        setVerticalAlignment(BOTTOM);
    }

    private void labelTypeTwo() {
        setFont(new Font("Courier New", Font.BOLD, 40));
        setForeground(new Color(249,186,72));
    }

    private void labelTypeOne() {
        setFont(new Font("Courier New", Font.BOLD, 20));
        setForeground(new Color(249,186,72));
    }
}