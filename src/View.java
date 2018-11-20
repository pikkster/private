import AccessPoints.Door;
import AccessPoints.automatic_door;
import Visual.RightPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.GroupLayout.*;

class View extends JFrame {

    /*
    WINDOW
     */
    private Controller contr;
    private int DEFAULT_HEIGHT = 600;
    private int DEFAULT_WIDTH = 800;
    private JButton addButton;
    private JButton info;
    private JButton insert;
    private JTextArea outputTextArea;
    private JPanel RIGHTPANEL;
    private JPanel LEFTPANEL;
    private JToolBar toolbar;
    private JMenuBar jMenubar;
    private JMenu menu1, menu2, menu3;
    private JMenuItem menuItem;
    private JTextField searchField;

    private int i = 0;
    private boolean LOGGING = false;

    private Object[] options = {"Yes", "No", "Cancel"};
    private String[] locations = {"", "Main", "Side", "Hall", "Garage"};
    final static String DO_ACTION = "do-search";

    View (Controller controller) {
        super();
        contr = controller;
        jMenubar = createMenubar();

        LEFTPANEL = createLeftPanel();

        RIGHTPANEL = new RightPanel(DEFAULT_HEIGHT,DEFAULT_WIDTH);

        toolbar = createToolbar();

        RIGHTPANEL.add(toolbar, BorderLayout.PAGE_END);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                LEFTPANEL, RIGHTPANEL);

        splitPane.setDividerLocation(DEFAULT_WIDTH /3);
        //KeyListener for searchfield
        InputMap im = searchField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = searchField.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), DO_ACTION);
        am.put(DO_ACTION, new doSearchAction());

        this.setJMenuBar(jMenubar);
        this.add(splitPane);

        this.setTitle("Operating View");
        this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void VIEW_UPDATER() {
        RIGHTPANEL.revalidate();
        RIGHTPANEL.repaint();
    }


    private JTextArea createInfoText() {
        JTextArea newInfoText = new JTextArea();
        newInfoText.setSize(200,DEFAULT_HEIGHT);
        newInfoText.setEditable(false);
        newInfoText.setLineWrap(true);
        newInfoText.append("Output Field");
        return newInfoText;
    }

    //TOOLBAR UNDER RIGHTPANEL
    private JToolBar createToolbar() {
        addButton = createAddButton();
        info = createInfoButton();
        insert = createInsertButton();
        JToolBar newToolbar = new JToolBar();
        newToolbar.add(addButton);
        newToolbar.add(info);
        newToolbar.add(insert);
        return newToolbar;
    }
    private JButton createAddButton() {
        JButton newAddButton = new JButton("ADD");
        newAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptForNewEntity();
            }
        });
        return newAddButton;
    }
    private JButton createInfoButton() {
        JButton newInfoButton = new JButton("Info");
        newInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText(contr.getAllStudents());

            }
        });
        return newInfoButton;
    }
    private JButton createInsertButton() {
        JButton newInsertButton = new JButton("+");
        newInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RIGHTPANEL.add(contr.getEntityList().get(i++));
                } catch (Exception wi) {
                    //
                }
                RIGHTPANEL.revalidate();
                RIGHTPANEL.repaint();
            }
        });
        return newInsertButton;
    }

    //MENUBAR
    private JMenuBar createMenubar() {
        JMenuBar newJMenuBar = new JMenuBar();
        menu1 = new JMenu("File");
        menu1.setMnemonic(KeyEvent.VK_A);
        menu1.getAccessibleContext().setAccessibleDescription("File menu");
        menu2 = new JMenu("Edit");
        menu2.setMnemonic(KeyEvent.VK_S);
        menu2.getAccessibleContext().setAccessibleDescription("Edit menu");
        menu3 = new JMenu("Help");
        menu3.setMnemonic(KeyEvent.VK_D);
        menu3.getAccessibleContext().setAccessibleDescription("HELP");

        //FILE MENU OPTIONS
        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RIGHTPANEL.add(contr.drawImage());
                RIGHTPANEL.revalidate();
                RIGHTPANEL.repaint();
            }
        });
        menu1.add(menuItem);
        menuItem = new JMenuItem("Save");
        menu1.add(menuItem);
        menuItem = new JMenuItem("Log");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("");
                LOGGING = true;
            }
        });
        menu1.add(menuItem);
        menuItem = new JMenuItem("Stop Log");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGING = false;
            }
        });
        menu1.add(menuItem);
        menu1.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showOptionDialog(null,"You want to quit?",
                        "Quit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,null,
                        options,
                        options[2]);
                if (choice == 0) {
                    System.exit(0);
                }else if(choice == 1) {
                    //not handled
                }else {
                    //not handled
                }
            }
        });
        menu1.add(menuItem);


        //EDIT MENU OPTIONS
        menuItem = new JMenuItem("Edit Object");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    promptForEdit();
            }
        });
        menu2.add(menuItem);
        menuItem = new JMenuItem("Add Object");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptForNewEntity();
                RIGHTPANEL.revalidate();
                RIGHTPANEL.repaint();
            }
        });
        menu2.add(menuItem);
        menuItem = new JMenuItem("Delete Object");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText("OUTPUTS ALL ENTITIES");
                outputTextArea.setText(contr.getEntitiesAsString());
                LOGGING = false;
            }
        });
        menu2.add(menuItem);

        newJMenuBar.add(menu1);
        newJMenuBar.add(menu2);
        newJMenuBar.add(menu3);

        return newJMenuBar;
    }

    void addLogToOutput(String log) {
        if(LOGGING) {
            outputTextArea.append(log);
        }
    }
    private void promptForNewEntity() {
        JTextField name = new JTextField();
        JTextField id = new JTextField();
        JTextField x_pos = new JTextField();
        JTextField y_pos = new JTextField();
        JComboBox location = new JComboBox(locations);

        Object[] message = {"Name", name,"ID", id,
        "X pos", x_pos, "Y pos", y_pos, "Location", location};

        int result = JOptionPane.showConfirmDialog(null,
                message, "NEW", JOptionPane.OK_CANCEL_OPTION);

        if(result == JOptionPane.OK_OPTION) {
            RIGHTPANEL.add(contr.createShape(name.getText(),id.getText(),
                    (String) location.getSelectedItem(),
                    x_pos.getText(),y_pos.getText()));
        }
        VIEW_UPDATER();
        this.repaint();
    }

    private void promptForEdit () {
        String[] temp = new String[contr.getEntityList().size()];
        for (int i = 0; i < contr.getEntityList().size(); i++) {
            temp[i] = contr.getEntityList().get(i).getName();
        }
        JComboBox existing = new JComboBox(temp);

        int result = JOptionPane.showConfirmDialog(null,
                existing, "Entity to edit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            //contr.changeColor((String) existing.getSelectedItem());
        }
        RIGHTPANEL.revalidate();
        RIGHTPANEL.repaint();
    }

    private JPanel createLeftPanel() {

        JLabel SEARCH = new JLabel("SEARCH:");
        searchField = new JTextField();
        outputTextArea = createInfoText();
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        JButton doSearch = new JButton("?");
        doSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTextArea.setText(contr.getStudentByIDNAME(searchField.getText()));
            }
        });

        JPanel TESTPANEL = new JPanel();
        GroupLayout layout = new GroupLayout(TESTPANEL);
        TESTPANEL.setLayout(layout);

        ParallelGroup hGroup = layout.createParallelGroup(Alignment.LEADING);

        SequentialGroup h1 = layout.createSequentialGroup();
        ParallelGroup h2 = layout.createParallelGroup(Alignment.TRAILING);

        h1.addContainerGap();

        h2.addComponent(scrollPane, Alignment.LEADING);

        SequentialGroup h3 = layout.createSequentialGroup();
        h3.addComponent(SEARCH);
        h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h3.addComponent(searchField, GroupLayout.DEFAULT_SIZE,321,Short.MAX_VALUE);
        h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        h3.addComponent(doSearch);

        h2.addGroup(h3);
        h1.addGroup(h2);

        h1.addContainerGap();

        hGroup.addGroup(Alignment.TRAILING,h1);
        layout.setHorizontalGroup(hGroup);

        //Create a parallel group for the vertical axis
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        //Create a sequential group v1
        SequentialGroup v1 = layout.createSequentialGroup();
        //Add a container gap to the sequential group v1
        v1.addContainerGap();
        //Create a parallel group v2
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        v2.addComponent(SEARCH);
        v2.addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        v2.addComponent(doSearch);
        //Add the group v2 tp the group v1
        v1.addGroup(v2);
        v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
        v1.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
        v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
        v1.addContainerGap();

        //Add the group v1 to the group vGroup
        vGroup.addGroup(v1);
        //Create the vertical group
        layout.setVerticalGroup(vGroup);

        return TESTPANEL;
    }

    class doSearchAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            outputTextArea.setText(contr.getStudentByIDNAME(searchField.getText()));
            LOGGING = false;
        }
    }
}
