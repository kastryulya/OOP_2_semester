package gui;

public class GameWindowRestored extends GameWindow {

  public GameWindowRestored() {
    super();
    restoreState();
  }

  public void setSize() {
    setSize(getDimension());
  }
}
