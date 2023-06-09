package ru.shchelkin.Frame.admin;

import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.Frame.ReportFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AdminMenu extends BackButtonFrame {
    public static final String title = "Админ меню";

    private static final JButton showTables = new JButton("Показать доступные справочники");
    private static final JButton showReport = new JButton("Посмотреть отчёты");
    private static final JButton searchClient = new JButton("Поиск клиента");

    private final AdminViewDataFrame viewDataFrame;
    private final ReportFrame reportFrame;
    private final SearchClientFrame searchClientFrame;

    public AdminMenu(AdminViewDataFrame viewDataFrame, ReportFrame reportFrame, SearchClientFrame searchClientFrame) throws HeadlessException {
        super(title);
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        int buttonMargin = 10;
        rightPanel.setLayout(new GridLayout(3, 1, buttonMargin, buttonMargin));

        Objects.requireNonNull(viewDataFrame);
        this.viewDataFrame = viewDataFrame;
        Objects.requireNonNull(reportFrame);
        this.reportFrame = reportFrame;
        Objects.requireNonNull(viewDataFrame);
        this.searchClientFrame = searchClientFrame;

        AddButtons();
    }

    private void AddButtons() {
        showFrameOnClick(showTables, viewDataFrame);
        showFrameOnClick(showReport, reportFrame);
        showFrameOnClick(searchClient, searchClientFrame);

        rightPanel.add(showTables);
        rightPanel.add(showReport);
        rightPanel.add(searchClient);
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
