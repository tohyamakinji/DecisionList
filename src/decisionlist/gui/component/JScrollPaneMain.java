package decisionlist.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JScrollPaneMain extends JScrollPane {

    public JScrollPaneMain(Component view, byte type) {
        super(view);
        switch (type) {
            case 1 :
                initializeTableTypeOne(view);
                break;
            case 2 :
                initializeTableTypeTwo(view);
                break;
            case 3 :
                initializeJTextAreaTypeOne(view);
                break;
        }
    }

    private void initializeJTextAreaTypeOne(Component view) {
        ((JTextArea) view).setEditable(false);
        ((JTextArea) view).setMargin(new Insets(10,10,10,10));
        view.setBackground(new Color(190, 191, 193));
        view.setFont(new Font("Courier New", Font.BOLD, 12));
        setPreferredSize(new Dimension(960,614));
        setBorder(BorderFactory.createEmptyBorder());
    }

    private void initializeTableTypeTwo(Component view) {
        ((JTable) view).setFillsViewportHeight(true);
        ((JTable) view).setPreferredScrollableViewportSize(new Dimension(940,595));
        view.setBackground(new Color(190, 191, 193));
        view.setFont(new Font("Courier New", Font.BOLD, 12));
        ((JTable) view).getTableHeader().setBackground(new Color(87,88,90));
        ((JTable) view).getTableHeader().setForeground(Color.WHITE);
        ((JTable) view).getTableHeader().setFont(new Font("Courier New", Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder());
    }

    private void initializeTableTypeOne(Component view) {
        ((JTable) view).setFillsViewportHeight(true);
        ((JTable) view).setPreferredScrollableViewportSize(new Dimension(940,540));
        view.setBackground(new Color(190, 191, 193));
        view.setFont(new Font("Courier New", Font.BOLD, 12));
        ((JTable) view).getTableHeader().setBackground(new Color(87,88,90));
        ((JTable) view).getTableHeader().setForeground(Color.WHITE);
        ((JTable) view).getTableHeader().setFont(new Font("Courier New", Font.BOLD, 13));
        setBorder(BorderFactory.createEmptyBorder());
    }

}