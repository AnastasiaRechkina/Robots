package gui;

import Model.Elements.IRobot;
import Model.Elements.Robot;
import Model.Model;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;

public class GameWindow extends JInternalFrame
{
    private GameVisualizer m_visualizer;
    private MainApplicationFrame view;
    private DefaultListModel<IRobot> robotsList;
    private StateWindow stateView = null;
    private Model model;

    GameWindow(int width, int height, MainApplicationFrame view) 
    {
        super("Игровое поле", true, true, true, true);
        this.view = view;
    
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
                if (m_visualizer != null)
                    m_visualizer.stop();
                if (stateView != null)
                    try {
                        stateView.setClosed(true);
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
            }
        });

        model = new Model(new Robot(30, 10));
        m_visualizer = new GameVisualizer(model);

        restructPane(m_visualizer);
        setSize(width, height);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(controlItem());
        return menuBar;
    }

    private void restructPane(GameVisualizer viser) {
        getContentPane().removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(viser, BorderLayout.CENTER);

        robotsList = new DefaultListModel<>();

        for (IRobot rob : getModel().getRobots())
            robotsList.add(0, rob);

        JList<IRobot> list = new JList<>(robotsList);

        list.addListSelectionListener(event -> {
            int index = list.getSelectedIndex();
            if (index != -1)
                viser.getModel().selectRobot(index);
        });

        panel.add(list, BorderLayout.WEST);
        getContentPane().add(panel);

        setJMenuBar(generateMenuBar());
        pack();
    }

    private JMenu controlItem() {
        JMenu menu = new JMenu("Управление");

        JMenuItem menuItem = new JMenuItem("Создать препятствие");
        menuItem.addActionListener(actionEvent -> m_visualizer.mouse = true);
        menu.add(menuItem);

        JMenuItem menuItemD = new JMenuItem("Создать робота");
        menuItemD.addActionListener(actionEvent -> {
            IRobot rob = new Robot(30, 10);
            robotsList.addElement(rob);
            getModel().addRobot(rob);
        });
        menu.add(menuItemD);

        JMenuItem menuItemR = new JMenuItem("Удалить робота");
        menuItemR.addActionListener(actionEvent -> {
            robotsList.removeElementAt(robotsList.getSize() - 1);
            getModel().removeRobot();
        });
        menu.add(menuItemR);

        return menu;
    }
    
    void setState(FrameState.InternalFrameState<GameWindow> state) throws PropertyVetoException {
        Model model = (Model) state.getInformation();
        changeVisualizer(model);
        if (state.params != null) {
            if (state.params.get("isMax") == 1) {
                setMaximum(true);
            } else {
                setSize(state.params.get("width"), state.params.get("height"));
            }

            setLocation(state.params.get("x"), state.params.get("y"));
            if (state.params.get("inFocus") == 1) setRequestFocusEnabled(true);
            if (state.params.get("isClosed") == 1) setClosed(true);
        }
    }
    
    public void changeVisualizer(Model model) {
        Dimension save = getSize();
        m_visualizer.stop();
        m_visualizer = new GameVisualizer(model);
        restructPane(m_visualizer);
        setSize(save);
    }

    public Model getModel() {
        return m_visualizer.getModel();
    }
}
