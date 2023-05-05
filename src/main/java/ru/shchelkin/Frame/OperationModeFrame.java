package ru.shchelkin.Frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class OperationModeFrame extends JFrame {
    public static final String title = "Выбор режима работы";

    private static final JButton adminButton = new JButton("Администратор");
    private static final JButton userButton = new JButton("Пользователь");

    private final JFrame adminMenu;
    private final JFrame userMenu;

    public OperationModeFrame(JFrame adminMenu, JFrame userMenu) throws HeadlessException {
        super(title);
        setSize(360, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        int buttonMargin = 10;
        int borderMargin = 20;
        JPanel panel = new JPanel(new GridLayout(2, 1, buttonMargin, buttonMargin));
        panel.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));

        Objects.requireNonNull(adminMenu);
        Objects.requireNonNull(userMenu);
        this.adminMenu = adminMenu;
        this.userMenu = userMenu;

        AddButtons(panel);
    }

    private void AddButtons(JPanel panel) {
        showFrameOnClick(adminButton, adminMenu);
        showFrameOnClick(userButton, userMenu);

        panel.add(adminButton);
        panel.add(userButton);

        add(panel);
    }

    private void showFrameOnClick(JButton button, JFrame frame) {
        button.addActionListener((e) -> {
            if (frame != null) {
                frame.setLocation(getLocation());
                frame.setVisible(true);
            }

            dispose();
        });
    }
}
