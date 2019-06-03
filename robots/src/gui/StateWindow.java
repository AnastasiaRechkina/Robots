package gui;

import Model.Model;

import javax.swing.JInternalFrame;
import java.awt.TextArea;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;

public class StateWindow extends JInternalFrame implements Observer {
    private final TextArea stateInfo = new TextArea("", 1, 1, TextArea.SCROLLBARS_NONE);

    public StateWindow(Model model) {
        super("Игровое поле", true, true, true, true);
        model.addObserver(this);
        stateInfo.setEnabled(false);
        getContentPane().add(stateInfo);
        setSize(200, 100);
        pack();
        onUpdateModel(model);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof Model)
            onUpdateModel((Model)observable);
    }

    public void onUpdateModel(Model model) {
        if (isVisible())
            EventQueue.invokeLater(() ->
                stateInfo.setText(
                        String.format("Robot: (%f, %f) \n Target: (%f, %f)",
                                      model.getRobot(0).getX(),
                                      model.getRobot(0).getY(),
                                      model.getTarget(0).getX(),
                                      model.getTarget(0).getY())));
    }
}
