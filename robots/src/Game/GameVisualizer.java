package Game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {

  private final Timer m_timer = initTimer();
  private final GameModel m_gameModel;
  private int width;
  private int height;

  private static Timer initTimer() {
    Timer timer = new Timer("events generator", true);
    return timer;
  }

  public GameVisualizer() {
    m_gameModel = new GameModel();
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onRedrawEvent();
      }
    }, 0, 50);
    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        m_gameModel.onModelUpdateEvent(width, height);
      }
    }, 0, 5);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        double scale = Toolkit.getDefaultToolkit().getScreenResolution() / 224.0;
        point.x = (int) (point.x / scale);
        point.y = (int) (point.y / scale);
        m_gameModel.setTargetPosition(point);
        repaint();
      }
    });
    setDoubleBuffered(true);
  }

  public GameModel getM_gameModel() {
    return m_gameModel;
  }

  protected void onRedrawEvent() {
    EventQueue.invokeLater(this::repaint);
  }

  private static int round(double value) {
    return (int) (value + 0.5);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    drawRobot(g2d, round(m_gameModel.getM_robotPositionX()),
        round(m_gameModel.getM_robotPositionY()), m_gameModel.getM_robotDirection());
    drawTarget(g2d, m_gameModel.getM_targetPositionX(), m_gameModel.getM_targetPositionY());
  }

  private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private void drawRobot(Graphics2D g, int x, int y, double direction) {
    int robotCenterX = round(m_gameModel.getM_robotPositionX());
    int robotCenterY = round(m_gameModel.getM_robotPositionY());
    AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);

    g.setTransform(t);
    g.setColor(Color.MAGENTA);
    fillOval(g, robotCenterX, robotCenterY, 30, 10);
    g.setColor(Color.BLACK);
    drawOval(g, robotCenterX, robotCenterY, 30, 10);
    g.setColor(Color.WHITE);
    fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
  }

  private void drawTarget(Graphics2D g, int x, int y) {
    AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
    g.setTransform(t);
    g.setColor(Color.GREEN);
    fillOval(g, x, y, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, x, y, 5, 5);
  }

  public void setTempWidth(int width) {
    this.width = width;
  }

  public void setTempHeight(int height) {
    this.height = height;
  }
}
