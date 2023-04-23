package gui;

import Locale.LanguageAdapter;
import Windows.GameWindow;
import Windows.GameWindowRestored;
import Windows.LogWindow;
import Windows.LogWindowRestored;
import Windows.RobotCoordinatesWindow;
import Windows.RobotCoordinatesWindowRestored;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;

import log.Logger;

public class MainApplicationFrame extends JFrame {

  private final JDesktopPane desktopPane = new JDesktopPane();
  private final LogWindow logWindow = createLogWindow(true);
  private final GameWindow gameWindow = createGameWindow(true);
  private final RobotCoordinatesWindow robotCoordinatesWindow = createRobotCoordinatesWindow(true);

  public MainApplicationFrame(LanguageAdapter adapter) {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    adapter.setLanguage();

    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset,
        screenSize.width - inset * 2,
        screenSize.height - inset * 2);

    setContentPane(desktopPane);

    setJMenuBar(generateMenuBar());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  protected RobotCoordinatesWindow createRobotCoordinatesWindow(boolean restoreFromBackup) {
    RobotCoordinatesWindow robotCoordinatesWindow;
    if (restoreFromBackup) {
      robotCoordinatesWindow = new RobotCoordinatesWindowRestored(gameWindow);
    } else {
      robotCoordinatesWindow = new RobotCoordinatesWindow(gameWindow);
    }

    desktopPane.add(robotCoordinatesWindow);
    robotCoordinatesWindow.setVisible(true);

    robotCoordinatesWindow.setSize();
    robotCoordinatesWindow.setLocation();
    setMinimumSize(robotCoordinatesWindow.getSize());

    try {
      robotCoordinatesWindow.setIcon(robotCoordinatesWindow.getStateIcon());
    } catch (PropertyVetoException ignored) {
    }
    return robotCoordinatesWindow;
  }

  protected LogWindow createLogWindow(boolean restoreFromBackup) {
    LogWindow logWindow;
    if (restoreFromBackup) {
      logWindow = new LogWindowRestored(Logger.getDefaultLogSource());
    } else {
      logWindow = new LogWindow(Logger.getDefaultLogSource());
    }

    desktopPane.add(logWindow);
    logWindow.setVisible(true);

    logWindow.setSize();
    logWindow.setLocation();
    setMinimumSize(logWindow.getSize());
//    logWindow.pack();

    Logger.debug("Протокол работает");

    try {
      logWindow.setIcon(logWindow.getStateIcon());
    } catch (PropertyVetoException e) {
      throw new RuntimeException(e);
    }
    return logWindow;
  }

  protected GameWindow createGameWindow(boolean restoreFromBackup) {
    GameWindow gameWindow;
    if (restoreFromBackup) {
      gameWindow = new GameWindowRestored();
    } else {
      gameWindow = new GameWindow();
    }

    desktopPane.add(gameWindow);
    gameWindow.setVisible(true);

    gameWindow.setSize();
    gameWindow.setLocation();
    setMinimumSize(gameWindow.getSize());

    try {
      gameWindow.setIcon(gameWindow.getStateIcon());
    } catch (PropertyVetoException ignored) {
    }
    return gameWindow;
  }

  protected JMenuItem generateJMenuItem(String text, int key, ActionListener action) {

    JMenuItem jmenuItem = new JMenuItem(text, key);
    jmenuItem.addActionListener(action);
    return jmenuItem;
  }

  protected JMenu generateLookAndFeelMenu() {
    JMenu lookAndFeelMenu = new JMenu("Режим отображения");
    lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
    lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
        "Управление режимом отображения приложения");

    lookAndFeelMenu.add(generateJMenuItem("Системная схема", KeyEvent.VK_S,
        (event) -> {
          setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          this.invalidate();
        }));

    lookAndFeelMenu.add(generateJMenuItem("Универсальная схема", KeyEvent.VK_S,
        (event) -> {
          setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
          this.invalidate();
        }));

    return lookAndFeelMenu;
  }

  protected JMenu generateTestMenu() {
    JMenu testMenu = new JMenu("Тесты");
    testMenu.setMnemonic(KeyEvent.VK_T);
    testMenu.getAccessibleContext().setAccessibleDescription(
        "Тестовые команды");

    testMenu.add(generateJMenuItem("Сообщение в лог", KeyEvent.VK_S,
        (event) -> {
          Logger.debug("Новая строка");
        }));

    return testMenu;
  }

  protected JMenu generateExitMenu() {
    JMenu exitMenu = new JMenu("Выход");

    exitMenu.setMnemonic(KeyEvent.VK_L);

    exitMenu.add(generateJMenuItem("Выход из игры", KeyEvent.VK_P,
        (event) -> {
          int result = JOptionPane.showConfirmDialog(null,
              "Вы действительно хотите выйти?",
              "Подтверждение выхода...",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE);
          if (result == JOptionPane.YES_OPTION) {
            this.logWindow.saveState(this.logWindow);
            this.gameWindow.saveState(this.gameWindow);
            this.robotCoordinatesWindow.saveState(this.robotCoordinatesWindow);
            System.exit(0);
          }
        }));

    return exitMenu;
  }

  private JMenuBar generateMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    menuBar.add(generateLookAndFeelMenu());
    menuBar.add(generateTestMenu());
    menuBar.add(generateExitMenu());
    return menuBar;
  }

  private void setLookAndFeel(String className) {
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException | InstantiationException
             | IllegalAccessException | UnsupportedLookAndFeelException e) {
    }
  }
}
