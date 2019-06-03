package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class LogWindowSource
{
    private int m_iQueueLength;
    
    private LinkedList<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
    	m_iQueueLength = iQueueLength;
        m_messages = new LinkedList<>();
        m_listeners = new ArrayList<LogChangeListener>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }
    
    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        addMessage(entry);
        onUpdateListeners();
    }

    public void onUpdateListeners() {
        if (m_activeListeners == null)
            synchronized (m_listeners) {
                if (m_activeListeners == null)
                    m_activeListeners = m_listeners.toArray(new LogChangeListener[0]);
            }
        for (LogChangeListener listener : m_activeListeners)
            listener.onLogChanged();
    }
    
    public void addMessage(LogEntry entry) {
        if (size() >= m_iQueueLength)
            m_messages.remove();
        m_messages.add(entry);
    }
    
    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> all()
    {
        return m_messages;
    }

    public void releaseListeners() {
        m_listeners.clear();
    }

    public void clear() {
        m_messages.clear();
    }
}
