package ru.shchelkin.Frame.admin;

import ru.shchelkin.Frame.BackButtonFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class AdminMenu extends BackButtonFrame {
    public static final String title = "Админ меню";

    private static final JButton showTables = new JButton("Показать доступные справочники");
    private static final JButton editTables = new JButton("Изменить доступные справочники");
    private static final JButton showReport = new JButton("Посомтреть отчёты");
    private static final JButton searchClient = new JButton("Поиск клиента");

    private final AdminViewDataFrame viewDataFrame;

    public AdminMenu(AdminViewDataFrame viewDataFrame) throws HeadlessException {
        super(title);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        int buttonMargin = 10;
        int borderMargin = 20;
        JPanel panel = new JPanel(new GridLayout(2, 1, buttonMargin, buttonMargin));
        panel.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));

        Objects.requireNonNull(viewDataFrame);
        this.viewDataFrame = viewDataFrame;

        AddButtons(panel);
    }

    private void AddButtons(JPanel panel) {
        showFrameOnClick(showTables, viewDataFrame);

        panel.add(showTables);

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
