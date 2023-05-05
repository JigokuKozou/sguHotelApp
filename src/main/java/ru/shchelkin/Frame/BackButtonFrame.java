package ru.shchelkin.Frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public abstract class BackButtonFrame extends JFrame {
    private final JButton backButton = new JButton("< Назад");
    private JFrame parentFrame;

    protected JPanel leftPanel = new JPanel();
    protected JPanel rightPanel = new JPanel();

    public BackButtonFrame(String title) {
        super(title);
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JPanel backButtonPanel = new JPanel();
        backButton.setPreferredSize(new Dimension(80, 30));
        backButtonPanel.setBorder(new EmptyBorder(5,10,5,10));
        backButtonPanel.add(backButton);

        var c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        add(backButtonPanel, c);

        final int defaultMargin = 10;
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        leftPanel.setBorder(new EmptyBorder(defaultMargin, defaultMargin, defaultMargin, defaultMargin));
        add(leftPanel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        rightPanel.setBorder(new EmptyBorder(defaultMargin, defaultMargin, defaultMargin, defaultMargin));
        add(rightPanel, c);
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(JFrame parentFrame) {
        Arrays.stream(backButton.getActionListeners())
                .forEach(backButton::removeActionListener);

        this.parentFrame = parentFrame;
        backButton.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setLocation(getLocation());
                parentFrame.setVisible(true);
            }
            dispose();
        });
    }
}
