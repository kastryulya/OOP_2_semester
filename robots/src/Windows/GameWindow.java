package Windows;

import Game.GameModel;
import Game.GameVisualizer;
import Save.WindowWithPathState;
import java.awt.BorderLayout;
import java.io.File;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class GameWindow extends WindowWithPathState implements Serializable {

  private final GameVisualizer m_visualizer;
  private final Timer m_timer = initTimer();

  private static Timer initTimer() {
    return new Timer("update size for m_visualizer", true);
  }

  public GameWindow() {
    super("Игровое поле", true, true, true, true,
        System.getProperty("user.home") + File.separator + "dataGameWindow.bin");
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();

    m_timer.schedule(new TimerTask() {
      @Override
      public void run() {
        m_visualizer.setTempWidth(panel.getWidth() * 2);
        m_visualizer.setTempHeight(panel.getHeight() * 2);
      }
    }, 0, 1);

    restoreState();
  }

  public GameModel getM_model() {
    return m_visualizer.getM_gameModel();
  }

  public void setSize() {
    setSize(getDimension());
  }
}
