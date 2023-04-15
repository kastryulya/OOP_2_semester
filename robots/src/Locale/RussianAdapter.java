package Locale;

import javax.swing.UIManager;

public class RussianAdapter implements LanguageAdapter {

  @Override
  public void setLanguage() {
    UIManager.put("OptionPane.yesButtonText", "Да");
    UIManager.put("OptionPane.noButtonText", "Нет");
  }
}
