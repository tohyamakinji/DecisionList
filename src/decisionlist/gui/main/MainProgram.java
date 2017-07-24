package decisionlist.gui.main;

import decisionlist.algorithm.Evaluation;
import decisionlist.analysis.TestingSet;
import decisionlist.analysis.TrainingSet;
import decisionlist.dataprocess.LatexWriter;
import decisionlist.gui.component.JButtonMain;
import decisionlist.gui.component.JLabelMain;
import decisionlist.gui.component.JProgressBarMain;
import decisionlist.gui.component.JScrollPaneMain;
import decisionlist.gui.loader.SenseLoader;
import decisionlist.gui.loader.TableLoader;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class MainProgram {

    private JFrame mainFrame;
    private JPanel panelTrainingContainerCard, rightPanelCard, panelTestingContainerCard, panelTestingCardOne, panelTestingCardTwo, panelTrainingCardOne, panelTrainingCardTwo, panelTrainingCardThree;
    private JScrollPane paneTableCollocation, paneTableCoOccurrence;
    private JLabel labelStartTraining, labelNavigationTwo, labelNavigationThree, gapOneTwo, gapTwoThree, labelNavigationOne, labelStartTesting, testingNavigationOne, testingNavigationTwo, gapOneTwoTesting, labelTrainingProgress, labelTestingProgress;
    private Color darkRedTypeOne = new Color(177,21,26), lightRedTypeOne = new Color(255, 77, 77);
    private JMenuItem itemLatexFormat, itemExcelFormat;
    private byte panelIdentifier = 0;
    private JProgressBar trainingProgressBar, testingProgressBar;
    private JComboBox<String> processBox, processTestingBox, senseBox;
    private JComboBox<Integer> rangeBox, rangeTestingBox;
    private ButtonGroup groupDebugMode;
    private JTable tableCollocation, tableCoOccurrence, tableDecisionList, tableTestingResult;
    private JTextArea areaResult;
    private JButton buttonViewResult;
    private boolean isFirstOccurrence, isFirstList, isWriting = false;

    // Debug purpose
    private boolean debugMode;

    private MainProgram(boolean debugMode) throws Exception {
        this.debugMode = debugMode;
        initComponents();
    }

    private void initComponents() throws IOException, ParseException {
        GridBagConstraints constraints = new GridBagConstraints();
        mainFrame = new JFrame("WSD - Decision List");
        // FONT UI MENU BAR
        Font font = new Font("Courier New", Font.BOLD, 12);
        // FILE MENU BAR
        JMenuBar menuBar = new JMenuBar();
        JMenu menuBarMenu = new JMenu("File");
        menuBarMenu.setFont(font);
        menuBarMenu.setMnemonic(KeyEvent.VK_F);
        JMenu menuSubBarMenu = new JMenu("Save As");
        menuSubBarMenu.setFont(font);
        itemLatexFormat = new JMenuItem("Latex Format (.txt)");
        itemLatexFormat.addActionListener(e -> {
            try {
                switch (panelIdentifier) {
                    case 1 :
                        latexWriter(panelIdentifier);
                        break;
                    case 2 :
                        latexWriter(panelIdentifier);
                        break;
                    case 3 :
                        latexWriter(panelIdentifier);
                        break;
                    case 4 :
                        latexWriter(panelIdentifier);
                        break;
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
        itemLatexFormat.setFont(font);
        menuSubBarMenu.add(itemLatexFormat);
        itemExcelFormat = new JMenuItem("Excel Format (.xlsx)");
        itemExcelFormat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (panelIdentifier) {
                    case 1 :
                        break;
                    case 2 :
                        break;
                    case 3 :
                        break;
                    case 4 :
                        break;
                }
            }
        });
        itemExcelFormat.setFont(font);
        menuSubBarMenu.add(itemExcelFormat);
        menuBarMenu.add(menuSubBarMenu);
        menuBarMenu.addSeparator();
        JMenuItem itemExit = new JMenuItem("Exit");
        itemExit.setFont(font);
        menuBarMenu.add(itemExit);
        menuBar.add(menuBarMenu);
        // TOOLS MENU BAR
        menuBarMenu = new JMenu("Tools");
        menuBarMenu.setFont(font);
        menuBarMenu.setMnemonic(KeyEvent.VK_T);
        menuSubBarMenu = new JMenu("Debug Mode");
        menuSubBarMenu.setFont(font);
        groupDebugMode = new ButtonGroup();
        JRadioButtonMenuItem debugYes = new JRadioButtonMenuItem("OFF");
        debugYes.setFont(font);
        debugYes.setSelected(true);
        groupDebugMode.add(debugYes);
        menuSubBarMenu.add(debugYes);
        JRadioButtonMenuItem debugNo = new JRadioButtonMenuItem("ON");
        debugNo.setFont(font);
        groupDebugMode.add(debugNo);
        menuSubBarMenu.add(debugNo);
        menuBarMenu.add(menuSubBarMenu);
        menuBar.add(menuBarMenu);
        // HELP MENU BAR
        menuBarMenu = new JMenu("Help");
        menuBarMenu.setFont(font);
        menuBarMenu.setMnemonic(KeyEvent.VK_H);
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/help_icon.png"));
        Image image = imageIcon.getImage().getScaledInstance(12,12, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JMenuItem itemHelp = new JMenuItem("Help", imageIcon);
        itemHelp.setFont(font);
        menuBarMenu.add(itemHelp);
        menuBarMenu.addSeparator();
        imageIcon = new ImageIcon(getClass().getResource("/images/about_icon.png"));
        image = imageIcon.getImage().getScaledInstance(12,12, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JMenuItem itemAbout = new JMenuItem("About", imageIcon);
        itemAbout.setFont(font);
        menuBarMenu.add(itemAbout);
        menuBar.add(menuBarMenu);
        // LEFT PANEL CONTAINER
        FlowLayout layoutLeftPanel = new FlowLayout();
        layoutLeftPanel.setVgap(0);
        JPanel leftPanelContainer = new JPanel(layoutLeftPanel);
        leftPanelContainer.setPreferredSize(new Dimension(300,500));
        leftPanelContainer.setOpaque(false);
        if (debugMode)
            leftPanelContainer.setOpaque(true);
        // TEL-U PANEL LOGO
        imageIcon = new ImageIcon(getClass().getResource("/images/telu_logo.png"));
        image = imageIcon.getImage().getScaledInstance(230,230,Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        JLabel label = new JLabel(imageIcon);
        JPanel panelTelU = new JPanel(new GridBagLayout());
        constraints.ipady = 15;
        panelTelU.add(label, constraints);
        panelTelU.setPreferredSize(new Dimension(300, 255));
        panelTelU.setBorder(BorderFactory.createMatteBorder(0,0,2,0, lightRedTypeOne));
        panelTelU.setOpaque(false);
        if (debugMode)
            panelTelU.setOpaque(true);
        leftPanelContainer.add(panelTelU);
        // TRAINING PANEL
        imageIcon = new ImageIcon(getClass().getResource("/images/training_icon.png"));
        image = imageIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        label = new JLabel(imageIcon);
        JPanel panelTraining = new JPanel(new GridBagLayout());
        panelTraining.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((CardLayout) rightPanelCard.getLayout()).show(rightPanelCard, "CardTraining");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!debugMode) {
                    panelTraining.setOpaque(true);
                    panelTraining.setBackground(lightRedTypeOne);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!debugMode) {
                    panelTraining.setOpaque(false);
                    panelTraining.setBackground(Color.WHITE);
                }
            }
        });
        constraints.gridx = 0;
        constraints.ipadx = 50;
        panelTraining.add(label, constraints);
        label = new JLabelMain("TRAINING DATA", (byte) 1);
        constraints.gridx = 1;
        constraints.ipadx = 54;
        panelTraining.add(label, constraints);
        panelTraining.setOpaque(false);
        if (debugMode) {
            panelTraining.setBackground(Color.magenta);
            panelTraining.setOpaque(true);
        }
        panelTraining.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTraining.setBorder(BorderFactory.createMatteBorder(0,0,1,0, lightRedTypeOne));
        leftPanelContainer.add(panelTraining);
        // TESTING PANEL
        JPanel panelTesting = new JPanel(new GridBagLayout());
        panelTesting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((CardLayout) rightPanelCard.getLayout()).show(rightPanelCard, "CardTesting");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!debugMode) {
                    panelTesting.setOpaque(true);
                    panelTesting.setBackground(lightRedTypeOne);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!debugMode) {
                    panelTesting.setOpaque(false);
                    panelTesting.setBackground(Color.WHITE);
                }
            }
        });
        imageIcon = new ImageIcon(getClass().getResource("/images/testing_icon.png"));
        image = imageIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        label = new JLabel(imageIcon);
        constraints.gridx = 0;
        constraints.ipadx = 50;
        panelTesting.add(label, constraints);
        label = new JLabelMain("TESTING DATA", (byte) 1);
        constraints.gridx = 1;
        constraints.ipadx = 66;
        panelTesting.add(label, constraints);
        panelTesting.setOpaque(false);
        if (debugMode) {
            panelTesting.setBackground(Color.blue);
            panelTesting.setOpaque(true);
        }
        panelTesting.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTesting.setBorder(BorderFactory.createMatteBorder(0,0,1,0, lightRedTypeOne));
        leftPanelContainer.add(panelTesting);
        // RESULT PANEL
        JPanel panelResult = new JPanel(new GridBagLayout());
        panelResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((CardLayout) rightPanelCard.getLayout()).show(rightPanelCard, "CardResult");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (!debugMode) {
                    panelResult.setOpaque(true);
                    panelResult.setBackground(lightRedTypeOne);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (!debugMode) {
                    panelResult.setOpaque(false);
                    panelResult.setBackground(Color.WHITE);
                }
            }
        });
        imageIcon = new ImageIcon(getClass().getResource("/images/result_icon.png"));
        image = imageIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        label = new JLabel(imageIcon);
        constraints.gridx = 0;
        constraints.ipadx = 50;
        panelResult.add(label, constraints);
        label = new JLabelMain("RESULT", (byte) 1);
        constraints.gridx = 1;
        constraints.ipadx = 138;
        panelResult.add(label, constraints);
        panelResult.setOpaque(false);
        if (debugMode) {
            panelResult.setBackground(Color.cyan);
            panelResult.setOpaque(true);
        }
        panelResult.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelResult.setBorder(BorderFactory.createMatteBorder(0,0,1,0, lightRedTypeOne));
        leftPanelContainer.add(panelResult);
        // LEFT PANEL
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint paint = new GradientPaint(0,0, new Color(195,1,1),0,150, new Color(149,1,1), true);
                graphics2D.setPaint(paint);
                graphics2D.fillRect(0,0, getWidth(), getHeight());
            }
        };
        ((FlowLayout)leftPanel.getLayout()).setVgap(0);
        ((FlowLayout)leftPanel.getLayout()).setHgap(0);
        leftPanel.setSize(300, 768);
        leftPanel.add(leftPanelContainer);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.add(leftPanel, BorderLayout.LINE_START);
        // RIGHT PANEL
        JPanel rightPanel = new JPanel();
        ((FlowLayout)rightPanel.getLayout()).setVgap(0);
        rightPanelCard = new JPanel(new CardLayout());
        rightPanelCard.setOpaque(false);
        // RIGHT PANEL TRAINING CONTAINER
        JPanel panelTrainingContainer = new JPanel();
        ((FlowLayout)panelTrainingContainer.getLayout()).setVgap(0);
        panelTrainingContainer.setOpaque(false);
        panelTrainingContainer.setPreferredSize(new Dimension(980, 768));
        // RIGHT PANEL TRAINING CONTAINER - NAVIGATOR
        JPanel panelTrainingComponent = new JPanel(new GridBagLayout());
        panelTrainingComponent.setOpaque(false);
        if (debugMode) {
            panelTrainingComponent.setBackground(Color.BLACK);
            panelTrainingComponent.setOpaque(true);
        }
        panelTrainingComponent.setPreferredSize(new Dimension(980, 90));
        imageIcon = new ImageIcon(getClass().getResource("/images/nav_one_active.png"));
        labelNavigationOne = new JLabel(imageIcon);
        labelNavigationOne.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((CardLayout) panelTrainingContainerCard.getLayout()).show(panelTrainingContainerCard, "TrainingCardOne");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_one_nonactive.png"));
                labelNavigationOne.setIcon(iconChange);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_one_active.png"));
                labelNavigationOne.setIcon(iconChange);
            }
        });
        labelNavigationOne.setCursor(new Cursor(Cursor.HAND_CURSOR));
        constraints.gridx = 0;
        constraints.ipadx = 40;
        constraints.ipady = 0;
        panelTrainingComponent.add(labelNavigationOne, constraints);
        gapOneTwo = new JLabel(".  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .");
        constraints.gridx = 1;
        constraints.ipadx = 0;
        panelTrainingComponent.add(gapOneTwo, constraints);
        imageIcon = new ImageIcon(getClass().getResource("/images/nav_two_nonactive.png"));
        labelNavigationTwo = new JLabel(imageIcon);
        labelNavigationTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gapOneTwo.getForeground().getRGB() == darkRedTypeOne.getRGB())
                    ((CardLayout) panelTrainingContainerCard.getLayout()).show(panelTrainingContainerCard, "TrainingCardTwo");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (gapOneTwo.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_two_nonactive.png"));
                    labelNavigationTwo.setIcon(iconChange);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (gapOneTwo.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_two_active.png"));
                    labelNavigationTwo.setIcon(iconChange);
                }
            }
        });
        constraints.gridx = 2;
        constraints.ipadx = 40;
        panelTrainingComponent.add(labelNavigationTwo, constraints);
        gapTwoThree = new JLabel(".  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .");
        constraints.gridx = 3;
        constraints.ipadx = 0;
        panelTrainingComponent.add(gapTwoThree, constraints);
        imageIcon = new ImageIcon(getClass().getResource("/images/nav_three_nonactive.png"));
        labelNavigationThree = new JLabel(imageIcon);
        labelNavigationThree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gapTwoThree.getForeground().getRGB() == darkRedTypeOne.getRGB())
                    ((CardLayout) panelTrainingContainerCard.getLayout()).show(panelTrainingContainerCard, "TrainingCardThree");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (gapTwoThree.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_three_nonactive.png"));
                    labelNavigationThree.setIcon(iconChange);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (gapTwoThree.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_three_active.png"));
                    labelNavigationThree.setIcon(iconChange);
                }
            }
        });
        constraints.gridx = 4;
        constraints.ipadx = 40;
        panelTrainingComponent.add(labelNavigationThree, constraints);
        panelTrainingContainer.add(panelTrainingComponent);
        // RIGHT PANEL TRAINING CONTAINER - CARD PANEL
        panelTrainingContainerCard = new JPanel(new CardLayout());
        panelTrainingContainerCard.setOpaque(false);
        // RIGHT PANEL TRAINING CONTAINER CARD - NAVIGATION ONE CARD
        panelTrainingCardOne = new JPanel(new GridBagLayout());
        panelTrainingCardOne.setPreferredSize(new Dimension(980,678));
        // RIGHT PANEL TRAINING NAVIGATION ONE (LABEL PRESS START)
        label = new JLabelMain("PRESS BUTTON TO START", (byte) 2);
        panelTrainingComponent = new JPanel(new GridBagLayout());
        panelTrainingComponent.setPreferredSize(new Dimension(980,50));
        panelTrainingComponent.add(label);
        panelTrainingComponent.setOpaque(false);
        if (debugMode) {
            panelTrainingComponent.setBackground(Color.GREEN);
            panelTrainingComponent.setOpaque(true);
        }
        // CONSTRAINT CARD ONE
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        panelTrainingCardOne.add(panelTrainingComponent, constraints);
        // RIGHT PANEL TRAINING NAVIGATION ONE (BUTTON START)
        panelTrainingComponent = new JPanel(new GridBagLayout());
        panelTrainingComponent.setPreferredSize(new Dimension(980,300));
        panelTrainingComponent.setOpaque(false);
        if (debugMode) {
            panelTrainingComponent.setBackground(Color.blue);
            panelTrainingComponent.setOpaque(true);
        }
        imageIcon = new ImageIcon(getClass().getResource("/images/start.png"));
        image = imageIcon.getImage().getScaledInstance(250,250, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        labelStartTraining = new JLabel(imageIcon);
        labelStartTraining.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    trainingButtonPressed();
                } catch (IOException | ParseException error) {
                    error.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelStartMouseEntered(labelStartTraining);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelStartMouseExited(labelStartTraining);
            }
        });
        labelStartTraining.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTrainingComponent.add(labelStartTraining, constraints);
        // RIGHT PANEL TRAINING NAVIGATION ONE (PROGRESS BAR AND LABEL PROCESS)
        trainingProgressBar = new JProgressBarMain();
        trainingProgressBar.setVisible(false);
        panelTrainingComponent.add(trainingProgressBar, constraints);
        labelTrainingProgress = new JLabelMain("NULL", (byte) 3);
        constraints.gridy = 1;
        labelTrainingProgress.setVisible(false);
        panelTrainingComponent.add(labelTrainingProgress, constraints);
        // CONSTRAINT CARD ONE
        constraints.gridx = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weighty = 0;
        panelTrainingCardOne.add(panelTrainingComponent, constraints);
        // RIGHT PANEL TRAINING NAVIGATION ONE (PROCESS TYPE AND RANGE)
        panelTrainingComponent = new JPanel(new GridBagLayout());
        label = new JLabelMain("COLLOCATION TYPE", (byte) 1);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 90;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panelTrainingComponent.add(label, constraints);
        processBox = new JComboBox<>(new String[]{"PARTIAL","THOROUGH"});
        processBox.setPreferredSize(new Dimension(200,30));
        processBox.setSelectedIndex(0);
        constraints.ipadx = 0;
        constraints.gridy = 1;
        constraints.ipady = 0;
        panelTrainingComponent.add(processBox, constraints);
        label = new JLabelMain("RANGE", (byte) 1);
        constraints.ipadx = 90;
        constraints.ipady = 30;
        constraints.gridx = 1;
        constraints.gridy = 0;
        panelTrainingComponent.add(label, constraints);
        rangeBox = new JComboBox<>(new Integer[]{1,2,3,4});
        rangeBox.setSelectedIndex(2);
        rangeBox.setPreferredSize(new Dimension(200,30));
        constraints.gridy = 1;
        constraints.ipady = 0;
        constraints.ipadx = 0;
        panelTrainingComponent.add(rangeBox, constraints);
        // CONSTRAINT CARD ONE
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        panelTrainingComponent.setOpaque(false);
        if (debugMode)
            panelTrainingComponent.setOpaque(true);
        panelTrainingCardOne.add(panelTrainingComponent, constraints);
        panelTrainingCardOne.setOpaque(false);
        if (debugMode) {
            panelTrainingCardOne.setBackground(Color.CYAN);
            panelTrainingCardOne.setOpaque(true);
        }
        panelTrainingCardOne.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                componentChangeIdentifier(false, (byte) 0);
            }
        });
        panelTrainingContainerCard.add(panelTrainingCardOne, "TrainingCardOne");
        // RIGHT PANEL TRAINING NAVIGATION TWO (UPPER BUTTON CONTROL)
        panelTrainingCardTwo = new JPanel(new GridBagLayout());
        panelTrainingCardTwo.setOpaque(false);
        panelTrainingComponent = new JPanel(new GridBagLayout());
        panelTrainingComponent.setPreferredSize(new Dimension(980, 55));
        JButton buttonCollocationResult = new JButtonMain("COLLOCATION RESULT", (byte) 1);
        buttonCollocationResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonCollocationResultPressed();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weighty = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        panelTrainingComponent.add(buttonCollocationResult, constraints);
        JButton buttonCoOccurrenceResult = new JButtonMain("CO-OCCURRENCE RESULT", (byte) 1);
        buttonCoOccurrenceResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonCoOccurrenceResultPressed();
            }
        });
        constraints.gridx = 1;
        panelTrainingComponent.add(buttonCoOccurrenceResult, constraints);
        JButton buttonUpdateDecisionList = new JButtonMain("UPDATE DECISION LIST", (byte) 1);
        buttonUpdateDecisionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                buttonUpdateDecisionListPressed();
            }
        });
        constraints.gridx = 2;
        panelTrainingComponent.add(buttonUpdateDecisionList, constraints);
        panelTrainingComponent.setOpaque(false);
        if (debugMode) {
            panelTrainingComponent.setBackground(Color.blue);
            panelTrainingComponent.setOpaque(true);
        }
        // CONSTRAINT CARD TWO
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        panelTrainingCardTwo.add(panelTrainingComponent, constraints);
        // RIGHT PANEL TRAINING NAVIGATION TWO (TABLE)
        DefaultTableModel modelCollocation = new DefaultTableModel(new Object[][]{{"[NULL][NULL][NULL]","[NULL]"}}, new Object[]{"TEXT", "COLLOCATION VECTOR"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCollocation = new JTable(modelCollocation);
        paneTableCollocation = new JScrollPaneMain(tableCollocation, (byte) 1);
        DefaultTableModel modelCoOccurrence = new DefaultTableModel(new Object[][]{{"[NULL]","[NULL]","[NULL]","[NULL]"}}, new Object[]{"POS","SENSE","FEATURE","OCCURRENCE"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCoOccurrence = new JTable(modelCoOccurrence);
        paneTableCoOccurrence = new JScrollPaneMain(tableCoOccurrence, (byte) 1);
        // CONSTRAINT CARD TWO
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        panelTrainingCardTwo.add(paneTableCollocation, constraints);
        panelTrainingCardTwo.add(paneTableCoOccurrence, constraints);
        panelTrainingCardTwo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (paneTableCollocation.isVisible())
                    componentChangeIdentifier(true, (byte) 1);
                else if (paneTableCoOccurrence.isVisible())
                    componentChangeIdentifier(true, (byte) 2);
            }
        });
        panelTrainingContainerCard.add(panelTrainingCardTwo, "TrainingCardTwo");
        // RIGHT PANEL TRAINING NAVIGATION THREE
        DefaultTableModel modelDecisionList = new DefaultTableModel(new Object[][]{{"[NULL]","[NULL]","[NULL]","[NULL]"}}, new Object[]{"POS","FEATURE","SENSE","LOG-L"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDecisionList = new JTable(modelDecisionList);
        JScrollPane paneTableDecisionList = new JScrollPaneMain(tableDecisionList, (byte) 2);
        panelTrainingCardThree = new JPanel(new GridBagLayout());
        panelTrainingCardThree.setOpaque(false);
        // CONSTRAINT CARD THREE
        constraints.gridy = 0;
        constraints.weighty = 1;
        panelTrainingCardThree.add(paneTableDecisionList, constraints);
        panelTrainingCardThree.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                componentChangeIdentifier(true, (byte) 3);
            }
        });
        panelTrainingContainerCard.add(panelTrainingCardThree, "TrainingCardThree");
        // LAST RIGHT PANEL CONTAINER
        panelTrainingContainer.add(panelTrainingContainerCard);
        panelTrainingContainer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (panelTrainingCardOne.isVisible())
                    componentChangeIdentifier(false, (byte) 0);
                else if (panelTrainingCardTwo.isVisible()) {
                    if (paneTableCollocation.isVisible())
                        componentChangeIdentifier(true, (byte) 1);
                    else if (paneTableCoOccurrence.isVisible())
                        componentChangeIdentifier(true, (byte) 2);
                } else if (panelTrainingCardThree.isVisible())
                    componentChangeIdentifier(true, (byte) 3);
            }
        });
        // LAST RIGHT PANEL ROOT CARD CONTAINER
        rightPanelCard.add(panelTrainingContainer, "CardTraining");
        // RIGHT PANEL TESTING CONTAINER
        JPanel panelTestingContainer = new JPanel();
        ((FlowLayout) panelTestingContainer.getLayout()).setVgap(0);
        panelTestingContainer.setPreferredSize(new Dimension(980, 768));
        panelTestingContainer.setOpaque(false);
        // RIGHT PANEL TESTING CONTAINER - NAVIGATOR
        JPanel panelTestingComponent = new JPanel(new GridBagLayout());
        panelTestingComponent.setOpaque(false);
        panelTestingComponent.setPreferredSize(new Dimension(980, 90));
        if (debugMode) {
            panelTestingComponent.setBackground(Color.GREEN);
            panelTestingComponent.setOpaque(true);
        }
        imageIcon = new ImageIcon(getClass().getResource("/images/nav_one_active.png"));
        testingNavigationOne = new JLabel(imageIcon);
        testingNavigationOne.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((CardLayout) panelTestingContainerCard.getLayout()).show(panelTestingContainerCard, "TestingCardOne");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_one_nonactive.png"));
                testingNavigationOne.setIcon(iconChange);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_one_active.png"));
                testingNavigationOne.setIcon(iconChange);
            }
        });
        testingNavigationOne.setCursor(new Cursor(Cursor.HAND_CURSOR));
        constraints.ipadx = 40;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        panelTestingComponent.add(testingNavigationOne, constraints);
        gapOneTwoTesting = new JLabel(".  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .");
        constraints.gridx = 1;
        constraints.ipadx = 0;
        panelTestingComponent.add(gapOneTwoTesting, constraints);
        imageIcon = new ImageIcon(getClass().getResource("/images/nav_two_nonactive.png"));
        testingNavigationTwo = new JLabel(imageIcon);
        testingNavigationTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gapOneTwoTesting.getForeground().getRGB() == darkRedTypeOne.getRGB())
                    ((CardLayout) panelTestingContainerCard.getLayout()).show(panelTestingContainerCard, "TestingCardTwo");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (gapOneTwoTesting.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_two_nonactive.png"));
                    testingNavigationTwo.setIcon(iconChange);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (gapOneTwoTesting.getForeground().getRGB() == darkRedTypeOne.getRGB()) {
                    ImageIcon iconChange = new ImageIcon(getClass().getResource("/images/nav_two_active.png"));
                    testingNavigationTwo.setIcon(iconChange);
                }
            }
        });
        constraints.gridx = 2;
        constraints.ipadx = 40;
        panelTestingComponent.add(testingNavigationTwo, constraints);
        panelTestingContainer.add(panelTestingComponent);
        // RIGHT PANEL TESTING CONTAINER - CARD PANEL
        panelTestingContainerCard = new JPanel(new CardLayout());
        panelTestingContainerCard.setOpaque(false);
        // RIGHT PANEL TESTING CONTAINER CARD - NAVIGATION ONE CARD
        panelTestingCardOne = new JPanel(new GridBagLayout());
        panelTestingCardOne.setOpaque(false);
        panelTestingCardOne.setPreferredSize(new Dimension(980,678));
        // RIGHT PANEL TESTING NAVIGATION ONE (LABEL PRESS START)
        label = new JLabelMain("PRESS BUTTON TO START", (byte) 2);
        panelTestingComponent = new JPanel(new GridBagLayout());
        panelTestingComponent.setOpaque(false);
        panelTestingComponent.setPreferredSize(new Dimension(980,50));
        constraints.gridx = 0;
        constraints.ipadx = 0;
        panelTestingComponent.add(label, constraints);
        if (debugMode) {
            panelTestingComponent.setBackground(Color.BLUE);
            panelTestingComponent.setOpaque(true);
        }
        // CONSTRAINT CARD ONE
        constraints.anchor = GridBagConstraints.NORTH;
        panelTestingCardOne.add(panelTestingComponent, constraints);
        // RIGHT PANEL TESTING NAVIGATION ONE (BUTTON START)
        panelTestingComponent = new JPanel(new GridBagLayout());
        panelTestingComponent.setPreferredSize(new Dimension(980,300));
        panelTestingComponent.setOpaque(false);
        if (debugMode) {
            panelTestingComponent.setBackground(Color.CYAN);
            panelTestingComponent.setOpaque(true);
        }
        imageIcon = new ImageIcon(getClass().getResource("/images/start.png"));
        image = imageIcon.getImage().getScaledInstance(250,250, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        labelStartTesting = new JLabel(imageIcon);
        labelStartTesting.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    buttonTestingPressed();
                } catch (IOException | ParseException error) {
                    error.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelStartMouseEntered(labelStartTesting);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelStartMouseExited(labelStartTesting);
            }
        });
        labelStartTesting.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTestingComponent.add(labelStartTesting, constraints);
        // RIGHT PANEL TESTING NAVIGATION ONE (PROGRESS BAR AND LABEL PROCESS)
        testingProgressBar = new JProgressBarMain();
        testingProgressBar.setVisible(false);
        panelTestingComponent.add(testingProgressBar, constraints);
        labelTestingProgress = new JLabelMain("NULL", (byte) 3);
        labelTestingProgress.setVisible(false);
        constraints.gridy = 1;
        panelTestingComponent.add(labelTestingProgress, constraints);
        // CONSTRAINT CARD ONE
        panelTestingCardOne.add(panelTestingComponent, constraints);
        // RIGHT PANEL TESTING NAVIGATION ONE (PROCESS TYPE AND RANGE)
        panelTestingComponent = new JPanel(new GridBagLayout());
        panelTestingComponent.setOpaque(false);
        if (debugMode) {
            panelTestingComponent.setBackground(Color.MAGENTA);
            panelTestingComponent.setOpaque(true);
        }
        label = new JLabelMain("COLLOCATION TYPE", (byte) 1);
        constraints.gridy = 0;
        constraints.ipady = 30;
        constraints.ipadx = 90;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panelTestingComponent.add(label, constraints);
        processTestingBox = new JComboBox<>(new String[]{"PARTIAL","THOROUGH"});
        processTestingBox.setPreferredSize(new Dimension(200,30));
        processTestingBox.setSelectedIndex(0);
        constraints.ipady = 0;
        constraints.ipadx = 0;
        constraints.gridy = 1;
        panelTestingComponent.add(processTestingBox, constraints);
        label = new JLabelMain("RANGE", (byte) 1);
        constraints.ipadx = 90;
        constraints.ipady = 30;
        constraints.gridx = 1;
        constraints.gridy = 0;
        panelTestingComponent.add(label, constraints);
        rangeTestingBox = new JComboBox<>(new Integer[]{1,2,3,4});
        rangeTestingBox.setSelectedIndex(2);
        rangeTestingBox.setPreferredSize(new Dimension(200,30));
        constraints.gridy = 1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        panelTestingComponent.add(rangeTestingBox, constraints);
        // CONSTRAINT CARD ONE
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;
        panelTestingCardOne.add(panelTestingComponent, constraints);
        panelTestingCardOne.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                componentChangeIdentifier(false, (byte) 0);
            }
        });
        panelTestingContainerCard.add(panelTestingCardOne, "TestingCardOne");
        // RIGHT PANEL TESTING NAVIGATION TWO
        DefaultTableModel modelTesting = new DefaultTableModel(new Object[][]{{"[NULL]","[NULL]","[NULL]","[NULL]","[NULL]"}}, new Object[]{"POS", "TEXT", "FEATURE RESULT", "SENSE","RESULT"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTestingResult = new JTable(modelTesting);
        JScrollPane paneTableTesting = new JScrollPaneMain(tableTestingResult, (byte) 2);
        panelTestingCardTwo = new JPanel(new GridBagLayout());
        panelTestingCardTwo.setOpaque(false);
        // CONSTRAINT CARD TWO
        constraints.gridy = 0;
        panelTestingCardTwo.add(paneTableTesting, constraints);
        panelTestingCardTwo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                componentChangeIdentifier(true, (byte) 4);
            }
        });
        panelTestingContainerCard.add(panelTestingCardTwo, "TestingCardTwo");
        // LAST RIGHT PANEL CONTAINER
        panelTestingContainer.add(panelTestingContainerCard);
        panelTestingContainer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (panelTestingCardOne.isVisible())
                    componentChangeIdentifier(false, (byte) 0);
                else if (panelTestingCardTwo.isVisible())
                    componentChangeIdentifier(true, (byte) 4);
            }
        });
        // LAST RIGHT PANEL ROOT CARD CONTAINER
        rightPanelCard.add(panelTestingContainer, "CardTesting");
        // RIGHT PANEL RESULT CONTAINER
        JPanel panelResultContainer = new JPanel();
        ((FlowLayout) panelResultContainer.getLayout()).setVgap(0);
        ((FlowLayout) panelResultContainer.getLayout()).setHgap(0);
        ((FlowLayout) panelResultContainer.getLayout()).setAlignment(FlowLayout.LEADING);
        panelResultContainer.setPreferredSize(new Dimension(980,768));
        panelResultContainer.setOpaque(false);
        // RIGHT PANEL RESULT CONTAINER - TARGET VIEW RESULT NAVIGATION
        JPanel panelResultComponent = new JPanel(new GridBagLayout());
        panelResultComponent.setPreferredSize(new Dimension(350, 90));
        panelResultComponent.setOpaque(false);
        if (debugMode) {
            panelResultComponent.setBackground(Color.MAGENTA);
            panelResultComponent.setOpaque(true);
        }
        senseBox = new JComboBox<>(new String[]{"NULL"});
        senseBox.setPreferredSize(new Dimension(200,40));
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0;
        panelResultComponent.add(senseBox, constraints);
        buttonViewResult = new JButtonMain("VIEW RESULT", (byte) 2);
        buttonViewResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    buttonViewResultPressed();
                } catch (IOException | ParseException error) {
                    error.printStackTrace();
                }
            }
        });
        constraints.gridx = 1;
        panelResultComponent.add(buttonViewResult, constraints);
        panelResultContainer.add(panelResultComponent);
        // RIGHT PANEL RESULT CONTAINER - TEXT AREA RESULT
        panelResultComponent = new JPanel(new GridBagLayout());
        panelResultComponent.setPreferredSize(new Dimension(980,678));
        panelResultComponent.setOpaque(false);
        if (debugMode) {
            panelResultComponent.setBackground(Color.CYAN);
            panelResultComponent.setOpaque(true);
        }
        areaResult = new JTextArea("NOTHING TO VIEW NOW\n\nTO SEE THE RESULT :\n\t1. CHOOSE SENSE THAT YOU WANT TO CHECK\n\t2. PRESS VIEW RESULT BUTTON\n\n\nCREATED BY TOHYAMA KINJI\nFURTHER QUESTION FEEL FREE TO SEND YOUR QUESTION AT : putugitaandika@gmail.com");
        JScrollPane paneAreaResult = new JScrollPaneMain(areaResult, (byte) 3);
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;
        panelResultComponent.add(paneAreaResult, constraints);
        panelResultContainer.add(panelResultComponent);
        // LAST RIGHT PANEL ROOT CARD CONTAINER
        panelResultContainer.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                componentChangeIdentifier(false, (byte) 0);
            }
        });
        rightPanelCard.add(panelResultContainer, "CardResult");
        rightPanel.add(rightPanelCard);
        // FRAME
        rightPanel.setBackground(new Color(162, 162, 169));
        mainFrame.add(rightPanel, BorderLayout.CENTER);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(1280, 768);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // LOADER
        initComboBoxSense();
        // COMPONENT CHANGE
        componentChangeIdentifier(false, (byte) 0);
    }

    private void latexWriter(byte b) throws IOException, ParseException {
        if (!isWriting) {
            switch (b) {
                case 1 :
                    int i = JOptionPane.showConfirmDialog(mainFrame, "You will save this collocation feature,\ncontinue?", "SAVING FILE", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        isWriting = true;
                        LatexWriter writer = new LatexWriter(this);
                        writer.saveTableCollocation();
                    }
                    break;
                case 2 :
                    int j = JOptionPane.showConfirmDialog(mainFrame, "You will save this co-occurrence feature,\ncontinue?", "SAVING FILE", JOptionPane.YES_NO_OPTION);
                    if (j == JOptionPane.YES_OPTION) {
                        isWriting = true;
                        LatexWriter writer = new LatexWriter(this);
                        writer.saveTableCoOccurrence(getDebugModeGroup().equals("ON"));
                    }
                    break;
                case 3 :
                    int k = JOptionPane.showConfirmDialog(mainFrame, "You will save this decision list,\ncontinue?", "SAVING FILE", JOptionPane.YES_NO_OPTION);
                    if (k == JOptionPane.YES_OPTION) {
                        isWriting = true;
                        LatexWriter writer = new LatexWriter(this);
                        writer.saveTableDecision(getDebugModeGroup().equals("ON"));
                    }
                    break;
                case 4 :
                    int l = JOptionPane.showConfirmDialog(mainFrame, "You will save this testing result,\ncontinue?", "SAVING FILE", JOptionPane.YES_NO_OPTION);
                    if (l == JOptionPane.YES_OPTION) {
                        isWriting = true;
                        LatexWriter writer = new LatexWriter(this);
                        writer.saveTestingResult(getDebugModeGroup().equals("ON"));
                    }
                    break;
            }
        }
    }

    public void doneWriting() {
        Toolkit.getDefaultToolkit().beep();
        isWriting = false;
    }

    private String getDebugModeGroup() {
        for (Enumeration<AbstractButton> elements = groupDebugMode.getElements(); elements.hasMoreElements();) {
            AbstractButton button = elements.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }
        return "UNDEFINED";
    }

    private void componentChangeIdentifier(boolean booleanChange, byte identifier) {
        itemLatexFormat.setEnabled(booleanChange);
        itemExcelFormat.setEnabled(booleanChange);
        panelIdentifier = identifier;
        if (debugMode)
            System.out.println("PANEL IDENTIFIER NOW = " + panelIdentifier);
    }

    private void buttonViewResultPressed() throws IOException, ParseException {
        if (buttonViewResult.isEnabled()) {
            buttonViewResult.setEnabled(false);
            Evaluation evaluation = new Evaluation(getDebugModeGroup().equals("ON"));
            evaluation.doGUIEvaluate(this);
        }
    }

    private void initComboBoxSense() throws IOException, ParseException {
        SenseLoader loader = new SenseLoader(this, getDebugModeGroup().equals("ON"));
        loader.loadSense();
    }

    private void buttonTestingPressed() throws IOException, ParseException {
        labelStartTesting.setVisible(false);
        testingProgressBar.setVisible(true);
        labelTestingProgress.setVisible(true);
        TestingSet set = new TestingSet(getDebugModeGroup().equals("ON"));
        set.process(this, (String) processTestingBox.getSelectedItem(), (Integer) rangeTestingBox.getSelectedItem());
    }

    public void doneTesting() {
        Toolkit.getDefaultToolkit().beep();
        testingProgressBar.setVisible(false);
        labelTestingProgress.setVisible(false);
        labelStartTesting.setVisible(true);
        ((CardLayout) panelTestingContainerCard.getLayout()).next(panelTestingContainerCard);
        testingProgressBar.setValue(testingProgressBar.getMinimum());
        ImageIcon changeImage = new ImageIcon(getClass().getResource("/images/nav_two_active.png"));
        testingNavigationTwo.setIcon(changeImage);
        testingNavigationTwo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gapOneTwoTesting.setForeground(darkRedTypeOne);
    }

    private void labelStartMouseExited(JLabel component) {
        ImageIcon changeImage = new ImageIcon(getClass().getResource("/images/start.png"));
        Image getChangeImage = changeImage.getImage().getScaledInstance(250,250, Image.SCALE_SMOOTH);
        changeImage = new ImageIcon(getChangeImage);
        component.setIcon(changeImage);
    }

    private void labelStartMouseEntered(JLabel component) {
        ImageIcon changeImage = new ImageIcon(getClass().getResource("/images/start_hover.png"));
        Image getChangeImage = changeImage.getImage().getScaledInstance(250,250, Image.SCALE_SMOOTH);
        changeImage = new ImageIcon(getChangeImage);
        component.setIcon(changeImage);
    }

    private void buttonUpdateDecisionListPressed() {
        ((CardLayout) panelTrainingContainerCard.getLayout()).next(panelTrainingContainerCard);
        ImageIcon changeImage = new ImageIcon(getClass().getResource("/images/nav_three_active.png"));
        labelNavigationThree.setIcon(changeImage);
        labelNavigationThree.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gapTwoThree.setForeground(darkRedTypeOne);
        // START TABLE LOADER
        if (isFirstList) {
            TableLoader loader = new TableLoader(getDebugModeGroup().equals("ON"));
            loader.decisionListTableLoader(this);
            isFirstList = false;
        }
    }

    private void buttonCoOccurrenceResultPressed() {
        paneTableCollocation.setVisible(false);
        paneTableCoOccurrence.setVisible(true);
        componentChangeIdentifier(true, (byte) 2);
        // START TABLE LOADER
        if (isFirstOccurrence) {
            TableLoader loader = new TableLoader(getDebugModeGroup().equals("ON"));
            loader.coOccurrenceTableLoader(this);
            isFirstOccurrence = false;
        }
    }

    private void buttonCollocationResultPressed() {
        paneTableCollocation.setVisible(true);
        paneTableCoOccurrence.setVisible(false);
        componentChangeIdentifier(true, (byte) 1);
    }

    private void trainingButtonPressed() throws IOException, ParseException {
        labelStartTraining.setVisible(false);
        trainingProgressBar.setVisible(true);
        labelTrainingProgress.setVisible(true);
        TrainingSet set = new TrainingSet(getDebugModeGroup().equals("ON"));
        set.process(this, (String) processBox.getSelectedItem(), (Integer) rangeBox.getSelectedItem());
    }

    public void doneTraining() {
        Toolkit.getDefaultToolkit().beep();
        trainingProgressBar.setVisible(false);
        labelTrainingProgress.setVisible(false);
        labelStartTraining.setVisible(true);
        ((CardLayout) panelTrainingContainerCard.getLayout()).next(panelTrainingContainerCard);
        trainingProgressBar.setValue(trainingProgressBar.getMinimum());
        ImageIcon changeImage = new ImageIcon(getClass().getResource("/images/nav_two_active.png"));
        labelNavigationTwo.setIcon(changeImage);
        labelNavigationTwo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gapOneTwo.setForeground(darkRedTypeOne);
        isFirstOccurrence = true;
        isFirstList = true;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JProgressBar getTrainingProgressBar() {
        return trainingProgressBar;
    }

    public JProgressBar getTestingProgressBar() {
        return testingProgressBar;
    }

    public JLabel getLabelTrainingProgress() {
        return labelTrainingProgress;
    }

    public JLabel getLabelTestingProgress() {
        return labelTestingProgress;
    }

    public JTable getTableCollocation() {
        return tableCollocation;
    }

    public JTable getTableCoOccurrence() {
        return tableCoOccurrence;
    }

    public JTable getTableDecisionList() {
        return tableDecisionList;
    }

    public JTable getTableTestingResult() {
        return tableTestingResult;
    }

    public JComboBox<String> getSenseBox() {
        return senseBox;
    }

    public JTextArea getAreaResult() {
        return areaResult;
    }

    public JButton getButtonViewResult() {
        return buttonViewResult;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainProgram(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}