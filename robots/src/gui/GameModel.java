package gui;

import java.awt.Point;
import java.util.Observable;

public class GameModel extends Observable {

  private volatile double m_robotPositionX = 0;
  private volatile double m_robotPositionY = 0;
  private volatile double m_robotDirection = 0;

  private volatile int m_targetPositionX = 800;
  private volatile int m_targetPositionY = 800;

  private static final double maxVelocity = 0.1;
  private static final double maxAngularVelocity = 0.001;
  public static final String KEY_ROBOT_POSITION_CHANGED = "position is changed";

  public GameModel() {
  }

  public double getM_robotPositionX() {
    return m_robotPositionX;
  }

  public double getM_robotPositionY() {
    return m_robotPositionY;
  }

  public double getM_robotDirection() {
    return m_robotDirection;
  }

  public int getM_targetPositionX() {
    return m_targetPositionX;
  }

  public int getM_targetPositionY() {
    return m_targetPositionY;
  }

  protected void setTargetPosition(Point p) {
    m_targetPositionX = p.x;
    m_targetPositionY = p.y;
  }

  private static double distance(double x1, double y1, double x2, double y2) {
    double diffX = x1 - x2;
    double diffY = y1 - y2;
    return Math.sqrt(diffX * diffX + diffY * diffY);
  }

  private static double angleTo(double fromX, double fromY, double toX, double toY) {
    double diffX = toX - fromX;
    double diffY = toY - fromY;

    if (diffY == 0) {
      return 0;
    }

    return asNormalizedRadians(Math.atan2(diffY, diffX));
  }

  protected void onModelUpdateEvent() {
    double distance = distance(m_targetPositionX, m_targetPositionY,
        m_robotPositionX, m_robotPositionY);
    if (distance < 0.5) {
      setChanged();
      notifyObservers(KEY_ROBOT_POSITION_CHANGED);
      clearChanged();
      return;
    }

    double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX,
        m_targetPositionY);
    double angularVelocity = 0;

    if (angleToTarget > m_robotDirection) {
      angularVelocity = maxAngularVelocity;
    }
    if (angleToTarget < m_robotDirection) {
      angularVelocity = -maxAngularVelocity;
    }
    moveRobot(maxVelocity, angularVelocity, 10);
  }

  private static double applyLimits(double value, double min, double max) {
    if (value < min) {
      return min;
    }
    return Math.min(value, max);
  }

  private void moveRobot(double velocity, double angularVelocity, double duration) {
    velocity = applyLimits(velocity, 0, maxVelocity);
    angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

    double newX = m_robotPositionX + velocity / angularVelocity *
        (Math.sin(m_robotDirection + angularVelocity * duration) -
            Math.sin(m_robotDirection));
    if (!Double.isFinite(newX)) {
      newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
    }

    double newY = m_robotPositionY - velocity / angularVelocity *
        (Math.cos(m_robotDirection + angularVelocity * duration) -
            Math.cos(m_robotDirection));
    if (!Double.isFinite(newY)) {
      newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
    }

    m_robotPositionX = newX;
    m_robotPositionY = newY;

    double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
    m_robotDirection = newDirection;

    setChanged();
    notifyObservers(KEY_ROBOT_POSITION_CHANGED);
    clearChanged();
  }

  private static double asNormalizedRadians(double angle) {
    while (angle < 0) {
      angle += 2 * Math.PI;
    }
    while (angle >= 2 * Math.PI) {
      angle -= 2 * Math.PI;
    }
    return angle;
  }

  public String getText() {
    return "Координаты робота: " + (int) getM_robotPositionX() + " " + (int) getM_robotPositionY()
        + "\n"
        + "Координаты цели: " + getM_targetPositionX() + " " + getM_targetPositionY();
  }
}
