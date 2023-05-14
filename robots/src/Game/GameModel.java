package Game;

import java.awt.Point;
import java.util.Observable;

public class GameModel extends Observable {

  private volatile double m_robotPositionX = 100;
  private volatile double m_robotPositionY = 100;
  private volatile double m_robotDirection = 0;

  private volatile int m_targetPositionX = 150;
  private volatile int m_targetPositionY = 100;


  private static double angle = 0;
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

  public void setTargetPosition(Point p) {
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

  public void onModelUpdateEvent(int width, int height) {
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
    double newAngle = asNormalizedRadians(angleToTarget - m_robotDirection);
    double angularVelocity = angularVelocity(angleToTarget);

    angle = newAngle;
    moveRobot(maxVelocity, angularVelocity, 10, width, height);
  }

  private static double applyLimits(double value, double min, double max) {
    if (value < min) {
      return min;
    }
    return Math.min(value, max);
  }

  private void moveRobot(double velocity, double angularVelocity, double duration, double width,
      double height) {
    velocity = applyLimits(velocity, 0, maxVelocity);
    double newDirection = asNormalizedRadians(
        m_robotDirection + Math.min(angle, angularVelocity) * duration);
    m_robotDirection = newDirection;
    double newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
    double newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
    m_robotPositionX = newX;
    m_robotPositionY = newY;

    setChanged();
    notifyObservers(KEY_ROBOT_POSITION_CHANGED);
    clearChanged();

    if (width != 0) {
      newX = applyLimits(m_robotPositionX, 0, width);
      m_robotPositionX = newX;
    }
    if (height != 0) {
      newY = applyLimits(m_robotPositionY, 0, height);
      m_robotPositionY = newY;
    }
  }


  public double angularVelocity(double angleToTarget) {
    double angularVelocity;
    if (Math.abs(m_robotDirection - angleToTarget) < 10e-7) {
      angularVelocity = 0;
    } else if (m_robotDirection >= Math.PI) {
      if (m_robotDirection - Math.PI < angleToTarget && angleToTarget < m_robotDirection) {
        angularVelocity = -maxAngularVelocity;
      } else {
        angularVelocity = maxAngularVelocity;
      }
    } else {
      if (m_robotDirection < angleToTarget && angleToTarget < m_robotDirection + Math.PI) {
        angularVelocity = maxAngularVelocity;
      } else {
        angularVelocity = -maxAngularVelocity;
      }
    }
    return angularVelocity;
  }

  private static double asNormalizedRadians(double angle) {
    return (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
  }

  public String getText() {
    return "Координаты робота: " + (int) getM_robotPositionX() + " " + (int) getM_robotPositionY()
        + "\n"
        + "Координаты цели: " + getM_targetPositionX() + " " + getM_targetPositionY();
  }
}
