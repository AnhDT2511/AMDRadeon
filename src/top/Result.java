package top;

/*
 * Decompiled with CFR 0_118.
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class Result extends Frame {

    public final int width = 400;
    public final int height = 45;
    public final int center = 200;
    private JLabel lblA;
    private JLabel lblQ;
    private JLabel lblQuery;
    Font font = new Font("Arial", Font.PLAIN , 11);

    public Result() {
        this.initComponents();
    }

    public JLabel getLblA() {
        return this.lblA;
    }

    public void setLblA(JLabel lblA) {
        this.lblA = lblA;
    }

    public JLabel getLblQ() {
        return this.lblQ;
    }

    public void setLblQ(JLabel lblQ) {
        this.lblQ = lblQ;
    }

    public JLabel getLblQuery() {
        return this.lblQuery;
    }

    public void setLblQuery(JLabel lblQuery) {
        this.lblQuery = lblQuery;
    }

    private void initComponents() {
        this.lblQuery = new JLabel();
        this.lblA = new JLabel();
        this.lblQ = new JLabel();
        this.lblA.setFont(font);
        this.lblQ.setFont(font);
        this.lblQuery.setFont(font);
        this.setAlwaysOnTop(true);
        this.setBackground(new Color(240, 240, 240));
        this.setBounds(new Rectangle((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45, width, 45));
        this.setExtendedState(0);
        this.setLocation(new Point((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45));
        this.setMaximizedBounds(new Rectangle((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45, width, 45));
        this.setMaximumSize(new Dimension(width, 45));
        this.setMinimumSize(new Dimension(width, 45));
        this.setUndecorated(true);
        this.setOpacity(0.3f);
        this.setPreferredSize(new Dimension(width, 45));
        this.setSize(new Dimension(width, 45));
        this.setState(0);
        this.setType(Window.Type.UTILITY);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                Result.this.exitForm(evt);
            }
        });
        this.lblQuery.setPreferredSize(new Dimension(width, 15));
        this.add((Component) this.lblQuery, "North");
        this.lblA.setPreferredSize(new Dimension(width, 15));
        this.add((Component) this.lblA, "South");
        this.lblQ.setPreferredSize(new Dimension(width, 15));
        this.add((Component) this.lblQ, "Center");
        this.pack();
    }

    private void exitForm(WindowEvent evt) {
        System.exit(0);
    }

    public void refresh() {
        this.dispose();
        this.lblQuery.setText("");
        this.lblA.setText("");
        this.lblQ.setText("");
        this.setAlwaysOnTop(true);
        this.setBackground(new Color(240, 240, 240));
        this.setBounds(new Rectangle((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45, width, 45));
        this.setExtendedState(0);
        this.setLocation(new Point((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45));
        this.setMaximizedBounds(new Rectangle((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - center, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 45, width, 45));
        this.setMaximumSize(new Dimension(width, 45));
        this.setMinimumSize(new Dimension(width, 45));
        this.setUndecorated(true);
        this.setOpacity(0.3f);
        this.setPreferredSize(new Dimension(width, 45));
        this.setSize(new Dimension(width, 45));
        this.setState(0);
        this.setType(Window.Type.UTILITY);
        this.lblQuery.setPreferredSize(new Dimension(width, 15));
        this.lblA.setPreferredSize(new Dimension(width, 15));
        this.lblQ.setPreferredSize(new Dimension(width, 15));
        this.pack();
    }

}
