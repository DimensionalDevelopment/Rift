package org.dimdev.riftloader.swing;

import javax.swing.*;
import java.awt.*;

public class GuiModLoaderProgress {

    private JLabel progressMessage = new JLabel("Initialising...");
    private JFrame mainFrame;
    private JProgressBar progressBar;

    private int x;
    private int y;

    public GuiModLoaderProgress(int x, int y) {
        this.x = x;
        this.y = y;
        JFrame frame = new JFrame("Rift Mod Loader");
        frame.setLayout(null);
        frame.setAutoRequestFocus(true);
        frame.resize(this.x, this.y);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        //
        JLabel label = new JLabel("Rift Mod Loader is preparing your Minecraft environment...");
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 18));
        setPosition(frame, label, (this.x / 2) - getCenteredStringWidth(label), 40);
        //
        progressMessage.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
        setPosition(frame, progressMessage, (this.x / 2 - getCenteredStringWidth(progressMessage)), this.y/2);
        frame.add(label);
        frame.add(progressMessage);
        //
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(this.x - 50, 25));
        progressBar.setIndeterminate(true);
        setPosition(frame, progressBar, (this.x / 2) - ((this.x - 50) / 2), this.y - 50);
        frame.add(progressBar);
        mainFrame = frame;
        frame.setVisible(true);
    }

    private void setPosition(JFrame pane, JComponent component, int x, int y) {
        Insets insets = pane.getInsets();
        Dimension size = component.getPreferredSize();
        component.setBounds(new Rectangle(x + insets.left, y + insets.top, size.width, size.height));
        //return new Rectangle(x + insets.left, y + insets.top, size.width, size.height);
    }

    private int getCenteredStringWidth(JLabel label) {
        FontMetrics fm = label.getFontMetrics(label.getFont());
        return fm.stringWidth(label.getText()) / 2;
    }

    public void updateProgressText(String info) {
        progressMessage.setText(info);
        setPosition(mainFrame, progressMessage, (this.x / 2 - getCenteredStringWidth(progressMessage)), this.y/2);
    }

    public void close(long wait) {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainFrame.setVisible(false);
    }

    public void setProgress(int done, int total) {
        if (done == -1) {
            progressBar.setIndeterminate(true);
            return;
        }
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(total);
        progressBar.setValue(done);
    }

}
