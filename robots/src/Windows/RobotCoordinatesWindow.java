package Windows;

import Game.GameModel;
import Save.WindowWithPathState;
import java.awt.BorderLayout;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RobotCoordinatesWindow extends WindowWithPathState implements Observer {

  private final JTextArea m_textField = new JTextArea("");
  private final GameModel m_gameModel;

  public RobotCoordinatesWindow(GameWindow gameWindow) {
    super("Координаты робота", true, true, true, true,
        System.getProperty("user.home") + File.separator + "RobotCoordinatesWindow.bin");

    m_gameModel = gameWindow.getM_model();
    m_gameModel.addObserver(this);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_textField, BorderLayout.CENTER);

    JPanel buttonsPanel = new JPanel(new BorderLayout());
    panel.add(buttonsPanel, BorderLayout.SOUTH);
    getContentPane().add(panel);
    pack();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (m_gameModel.equals(o)) {
      if (GameModel.KEY_ROBOT_POSITION_CHANGED.equals(arg)) {
        onRobotPositionChanged();
      }
    }
  }

  private void onRobotPositionChanged() {
    m_textField.setText(m_gameModel.getText());
  }

  public void setSize() {
    setSize(400, 400);
  }
}
