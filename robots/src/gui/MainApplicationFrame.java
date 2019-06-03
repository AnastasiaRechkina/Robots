package gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.LogEntry;
import log.LogLevel;
import log.Logger;

public class MainApplicationFrame extends JFrame{
    private JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,screenSize.width  - inset*2,screenSize.height - inset*2);
        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());  
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                String ObjButtons[] = {"Да", "Нет"};
                int PromptResult = JOptionPane.showOptionDialog(null,
                        "Вы уверены?",
                        null,
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        ObjButtons,
                        ObjButtons[1]);
                if (PromptResult == JOptionPane.YES_OPTION)
                    onClosing();
            }

            @Override
            public void windowOpened(WindowEvent windowEvent) {
                super.windowOpened(windowEvent);
                try {
                    onOpen();
                } catch (PropertyVetoException e) {
                    System.out.println("Error with privileges");
                }
            }
        });
    }  
    public void setState(FrameState state) throws PropertyVetoException {
        String description = "public class gui.";
        for (FrameState.InternalFrameState istate : state.getAllStates()) {
            if (istate.getTag().equals(description + "GameWindow")) {
                GameWindow game = new GameWindow(0, 0, this);
                addWindow(game);
                game.setState((FrameState.InternalFrameState<GameWindow>) istate);
            } else if (istate.getTag().equals(description + "LogWindow")) {
                LogWindow log = createNewLogWindow();
                addWindow(log);
                log.setState((FrameState.InternalFrameState<LogWindow>) istate);
            }
        }
    }

    private void onOpen() throws PropertyVetoException {
        ObjectInputStream ois = getStateFile();
        FrameState state = null;
        if (ois != null)
            try {
                state = (FrameState) ois.readObject();
            } catch (IOException e) {
                System.out.println("Load defaults");
            } catch (ClassNotFoundException e) {
                System.out.println("Load defaults");
            }
        if (state != null)
            setState(state);
    }
    
    public static ObjectInputStream getStateFile() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("save.state"));
        } catch (IOException ex) {
            return null;
        }
        return ois;
    }
    
    private void onClosing() {
        ObjectOutputStream oos = createStateStream();
        if (oos != null) {
            try {
                FrameState state = new FrameState(this);
                oos.writeObject(state);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Can't serialize object, not serializable error");
            }
            try {
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                System.out.println("Can't flush and close");
            }
        }
        System.exit(0);
    }
    
    private ObjectOutputStream createStateStream() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("save.state"));
        } catch (IOException ex) {
            File stFile = new File("save.state");
            try {
                stFile.createNewFile();
                oos = new ObjectOutputStream(new FileOutputStream("save.state"));
            } catch (IOException e) {
                return null;
            }
        }
        return oos;
    }
            
    protected LogWindow createNewLogWindow()
    {
    	Logger.getDefaultLogSource().releaseListeners();
        Logger.clear();
    	LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    public JInternalFrame[] getAllFrames() {
        return desktopPane.getAllFrames();
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(documentItem());
        return menuBar;
    }

    private JMenu documentItem() {
        JMenu menu = new JMenu("Игра");
        menu.setMnemonic(KeyEvent.VK_D);
        menu.add(newDoc());
        menu.add(newGame());
        menu.add(quitApp());
        return menu;
    }

    private JMenuItem newDoc() {
        JMenuItem menuItem = new JMenuItem("Логи");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.addActionListener(actionEvent -> addWindow(createNewLogWindow()));
        return menuItem;
    }

    private JMenuItem newGame() {
        JMenuItem menuItem = new JMenuItem("Новая игра");
        menuItem.setMnemonic(KeyEvent.VK_G);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        menuItem.addActionListener(actionEvent -> addWindow(new GameWindow(400, 400, this)));
        return menuItem;
    }

    private JMenuItem quitApp() {
        JMenuItem menuItem = new JMenuItem("Выход");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));

        menuItem.setActionCommand("Выход");
        menuItem.addActionListener(actionEvent ->
                Toolkit.getDefaultToolkit()
                        .getSystemEventQueue()
                        .postEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        return menuItem;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {}
    }}
