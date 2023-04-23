package Windows;

import log.LogWindowSource;

public class LogWindowRestored extends LogWindow {

  public LogWindowRestored(LogWindowSource logSource) {
    super(logSource);
    restoreState();
  }

  public void setSize() {
    setSize(getDimension());
  }

}
