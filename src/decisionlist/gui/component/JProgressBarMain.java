package decisionlist.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JProgressBarMain extends JProgressBar {

    public JProgressBarMain() {
        setBorder(BorderFactory.createEmptyBorder());
        setBorderPainted(false);
        setForeground(new Color(87,88,90));
        setPreferredSize(new Dimension(700,50));
        setStringPainted(true);
    }

}