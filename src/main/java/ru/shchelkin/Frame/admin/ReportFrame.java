package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.model.HotelsInfo;
import ru.shchelkin.util.EntityTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportFrame extends BackButtonFrame {
    public ReportFrame(JdbcTemplate jdbcTemplate) {
        super("Информация об отелях");


        showTable(getReportData(jdbcTemplate));
    }
    public List<HotelsInfo> getReportData(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(
                "SELECT * FROM HotelsInfo",
                new BeanPropertyRowMapper<>(HotelsInfo.class));
    }

    private void showTable(List<HotelsInfo> data) {
        EntityTable<HotelsInfo> table = new EntityTable<>(HotelsInfo.class, data);

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
