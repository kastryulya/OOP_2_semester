package Windows;

public class RobotCoordinatesWindowRestored extends RobotCoordinatesWindow{

  public RobotCoordinatesWindowRestored(GameWindow gameWindow) {
    super(gameWindow);
    restoreState();
  }

  public void setSize() {
    setSize(getDimension());
  }
}
