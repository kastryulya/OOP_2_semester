package Save;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.swing.JInternalFrame;

public class WindowWithPathState extends JInternalFrame implements ObjectWithState {

  private String path;
  private int x;
  private int y;
  private boolean isIcon = false;
  private Dimension dimension;

  public WindowWithPathState(String title, boolean b, boolean b1, boolean b2, boolean b3,
      String path) {
    super(title, b, b1, b2, b3);
    setPath(path);
  }

  public void setLocation() {
    setLocation(this.x, this.y);
  }

  public boolean getStateIcon() {
    return this.isIcon;
  }

  public void setSize() {
    setSize(getDimension());
  }

  public Dimension getDimension() {
    return this.dimension;
  }

  @Override
  public void saveState(JInternalFrame window) {
    String path = getPath();

    try (OutputStream os = new FileOutputStream(path)) {
      try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
        Point point = window.getLocation();
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
    String path = getPath();
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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
