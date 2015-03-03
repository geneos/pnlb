package test;

import java.awt.*;
import javax.swing.JPanel;

public class Panel1 extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();

  public Panel1() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setBackground(new Color(180, 180, 190));
    this.setLayout(borderLayout1);
  }
}