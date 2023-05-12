package ru.shchelkin.Frame;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.util.EntityTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportFrame extends BackButtonFrame {
    private final JComboBox<String> tableSelector;

    private final JdbcTemplate jdbcTemplate;

    public ReportFrame(JdbcTemplate jdbcTemplate, Map<String, Class<?>> views) {
        super("Отчёты");
        this.jdbcTemplate = jdbcTemplate;

        tableSelector = new JComboBox<>(views.keySet().toArray(new String[0]));
        tableSelector.addActionListener(e -> {
            String viewName = (String) tableSelector.getSelectedItem();
            if (viewName != null) {
                showTable(getReportData(viewName, views.get(viewName)), views.get(viewName));
            }
        });

        leftPanel.add(tableSelector, BorderLayout.NORTH);
    }

    public <T> List<T> getReportData(String viewName, Class<T> clazz) {
        return jdbcTemplate.query(
                "SELECT * FROM " + viewName,
                new BeanPropertyRowMapper<>(clazz));
    }

    private <T> void showTable(List<T> data, Class clazz) {
        EntityTable<T> table = new EntityTable<>(clazz, data);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel()); // создаем TableRowSorter

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(); // создаем список SortKey для сортировки по первой колонке
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys); // устанавливаем список SortKey для TableRowSorter

        table.setRowSorter(sorter); // устанавливаем TableRowSorter на таблицу

        JScrollPane scrollPane = new JScrollPane(table);

        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
