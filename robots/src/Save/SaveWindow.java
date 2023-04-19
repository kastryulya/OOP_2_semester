package Save;

import javax.swing.JInternalFrame;

public interface SaveWindow {
  public void saveState(JInternalFrame window);
  public void restoreState();
}
