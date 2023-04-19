package gui;

import Save.SaveWindow;
import java.awt.BorderLayout;

import java.awt.Dimension;
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
import java.io.Serializable;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements SaveWindow, Serializable {

  private final GameVisualizer m_visualizer;
  private int x;
  private int y;
  private boolean isIcon = false;
  private Dimension dimension;

  public GameWindow(boolean restoreFromBackup) {
    super("Игровое поле", true, true, true, true);

    if (restoreFromBackup) {
      restoreState();
    }

    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
  }
  public boolean getStateIcon() {
    return this.isIcon;
  }
  public void setLocation() {
    setLocation(this.x, this.y);
  }
  public Dimension getDimension(){
    return this.dimension;
  }

  @Override
  public void saveState(JInternalFrame window) {
    String path = System.getProperty("user.home") + File.separator + "dataGameWindow.bin";

    try (OutputStream os = new FileOutputStream(path)) {
      try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
        Point point = getLocation();
        oos.writeObject(point.x);
        oos.writeObject(point.y);
        oos.writeObject(window.isIcon());
        oos.writeObject(window.getSize());
        oos.flush();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void restoreState() {
    String path = System.getProperty("user.home") + File.separator + "dataGameWindow.bin";

    try (InputStream is = new FileInputStream(path)) {
      try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
        this.x = (int) ois.readObject();
        this.y = (int) ois.readObject();
        this.isIcon = (boolean) ois.readObject();
        this.dimension = (Dimension) ois.readObject();
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
