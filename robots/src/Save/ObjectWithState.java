package Save;

import javax.swing.JInternalFrame;

public interface ObjectWithState {
  void saveState(JInternalFrame window);
  void restoreState();
}
