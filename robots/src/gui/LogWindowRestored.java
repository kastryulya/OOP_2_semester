package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.File;
import javax.swing.JPanel;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindowRestored extends LogWindow {

  private final LogWindowSource m_logSource;
  private TextArea m_logContent;

  public LogWindowRestored(LogWindowSource logSource) {
    super(logSource);
    setPath(System.getProperty("user.home") + File.separator + "dataLogWindow.bin");

    m_logSource = logSource;
    m_logSource.registerListener(this);

    m_logContent = new TextArea("");
    restoreState();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_logContent, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
  }

  private void updateLogContent() {
    StringBuilder content = new StringBuilder();
    for (LogEntry entry : m_logSource.all()) {
      content.append(entry.getMessage()).append("\n");
    }
    m_logContent.setText(content.toString());
    m_logContent.invalidate();
  }

  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }

  public void setSize() {
    m_logContent.setSize(getDimension());
  }
}
