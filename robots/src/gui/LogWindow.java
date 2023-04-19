package gui;

import Save.SaveWindow;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.TextArea;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, SaveWindow {

  private LogWindowSource m_logSource;
  private TextArea m_logContent;
  private int x;
  private int y;
  private boolean isIcon = false;

  public LogWindow(LogWindowSource logSource, boolean restoreFromBackup) {
    super("Протокол работы", true, true, true, true);
    m_logSource = logSource;
    m_logSource.registerListener(this);

    if (restoreFromBackup) {
      restoreState();
    } else {
      m_logContent = new TextArea("");
      m_logContent.setSize(200, 500);
    }

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_logContent, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();

    if (!restoreFromBackup) {
      updateLogContent();
    }
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

  public void setLocation() {
    setLocation(this.x, this.y);
  }

  public boolean getStateIcon() {
    return this.isIcon;
  }

  @Override
  public void saveState(JInternalFrame window) {
    String path = System.getProperty("user.home") + File.separator + "dataLogWindow.bin";

    try (OutputStream os = new FileOutputStream(path)) {
      try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
        oos.writeObject(this.m_logContent);
        Point point = window.getLocation();
        oos.writeObject(point.x);
        oos.writeObject(point.y);
        oos.writeObject(window.isIcon());
        oos.flush();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void restoreState() {
    String path = System.getProperty("user.home") + File.separator + "dataLogWindow.bin";

    try (InputStream is = new FileInputStream(path)) {
      try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
        this.m_logContent = (TextArea) ois.readObject();
        this.x = (int) ois.readObject();
        this.y = (int) ois.readObject();
        this.isIcon = (boolean) ois.readObject();
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
