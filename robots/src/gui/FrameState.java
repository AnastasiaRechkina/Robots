package gui;

import javax.swing.JInternalFrame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FrameState implements Serializable {
    private ArrayList<InternalFrameState> states = new ArrayList<>();

    public FrameState(MainApplicationFrame frame) {
        for (JInternalFrame iframe : frame.getAllFrames())
            if (iframe instanceof GameWindow) {
                GameWindow game = (GameWindow) iframe;
                states.add(new InternalFrameState<>(game, game.getModel()));
            } else if (iframe instanceof LogWindow) {
                LogWindow log = (LogWindow) iframe;
                states.add(new InternalFrameState<>(log, log.getContent()));
            }
    }

    public Iterable<InternalFrameState> getAllStates() {
        return states;
    }

    class InternalFrameState<T extends JInternalFrame> implements Serializable {
        private String tag;
        public HashMap<String, Integer> params;
        private Object information;

        public InternalFrameState(T iframe, Object info) {
            tag = iframe.getClass().toGenericString();
            params = new HashMap<>();
            params.put("x", iframe.getX());
            params.put("y", iframe.getY());
            params.put("width", iframe.getWidth());
            params.put("height", iframe.getHeight());
            params.put("isMax", iframe.isMaximum() ? 1 : 0);
            params.put("isClosed", iframe.isClosed() ? 1 : 0);
            params.put("inFocus", iframe.isFocusOwner() ? 1 : 0);
            information = info;
        }

        public String getTag() {
            return tag;
        }

        public Object getInformation() {
            return information;
        }
    }
}