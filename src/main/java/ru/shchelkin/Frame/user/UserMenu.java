package ru.shchelkin.Frame.user;

import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.Frame.admin.ReportFrame;
import ru.shchelkin.Frame.admin.SearchFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class UserMenu extends BackButtonFrame {
    public static final String title = "Админ меню";

    private static final JButton showTables = new JButton("Показать доступные справочники");
    private static final JButton showReport = new JButton("Посомтреть отчёты");
    private static final JButton searchClient = new JButton("Поиск активных бронирований");

    private final UserViewDataFrame viewDataFrame;
    private final ReportFrame reportFrame;
    private final SearchFrame searchFrame;

    public UserMenu(UserViewDataFrame viewDataFrame, ReportFrame reportFrame, SearchFrame searchFrame) throws HeadlessException {
        super(title);
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        int buttonMargin = 10;
        int borderMargin = 20;
        JPanel panel = new JPanel(new GridLayout(2, 1, buttonMargin, buttonMargin));
        panel.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));

        Objects.requireNonNull(viewDataFrame);
        this.viewDataFrame = viewDataFrame;
        Objects.requireNonNull(reportFrame);
        this.reportFrame = reportFrame;
        Objects.requireNonNull(viewDataFrame);
        this.searchFrame = searchFrame;

        AddButtons(panel);
    }

    private void AddButtons(JPanel panel) {
        showFrameOnClick(showTables, viewDataFrame);
        showFrameOnClick(showReport, reportFrame);
        showFrameOnClick(searchClient, searchFrame);

        panel.add(showTables);
        panel.add(showReport);
        panel.add(searchClient);

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
