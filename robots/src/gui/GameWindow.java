package gui;

import Save.ObjectWithState;
import Save.WindowWithPathState;
import java.awt.BorderLayout;
import java.io.File;
import java.io.Serializable;
import javax.swing.JPanel;

public class GameWindow extends WindowWithPathState implements ObjectWithState, Serializable {

  private final GameVisualizer m_visualizer;

  public GameWindow() {
    super("Игровое поле", true, true, true, true,
        System.getProperty("user.home") + File.separator + "dataGameWindow.bin");
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
  }

  public void setSize() {
    setSize(400, 400);
  }
}
