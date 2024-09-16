package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class GameWindowRestored extends GameWindow {

  private final GameVisualizer m_visualizer;

  public GameWindowRestored() {
    super();
    restoreState();
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
  }

  public void setSize() {
    setSize(getDimension());
  }
}
