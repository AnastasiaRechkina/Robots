package log;

import java.io.Serializable;

public enum LogLevel implements Serializable {
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private int m_iLevel;
    
    private LogLevel(int iLevel)
    {
        m_iLevel = iLevel;
    }
    
    public int level()
    {
        return m_iLevel;
    }
}
