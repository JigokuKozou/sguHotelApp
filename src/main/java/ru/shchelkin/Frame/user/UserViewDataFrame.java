package ru.shchelkin.Frame.user;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.dao.Dao;
import ru.shchelkin.model.Client;
import ru.shchelkin.util.EditDialog;
import ru.shchelkin.util.EntityTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserViewDataFrame extends BackButtonFrame {
    private final Map<String, Dao<?>> daoMap = new LinkedHashMap<>();
    private final JComboBox<String> tableSelector;

    public UserViewDataFrame(JdbcTemplate jdbcTemplate) {
        super("Просмотр справочников");

        daoMap.put("Client", new Dao<>(Client.class, jdbcTemplate));

        leftPanel.setLayout(new BorderLayout());

        tableSelector = new JComboBox<>(daoMap.keySet().toArray(new String[0]));
        tableSelector.addActionListener(e -> {
            String tableName = (String) tableSelector.getSelectedItem();
            if (tableName != null) {
                Dao<?> dao = daoMap.get(tableName);
                if (dao != null) {
                    showTable(dao);
                }
            }
        });
        leftPanel.add(tableSelector, BorderLayout.NORTH);
    }

    private <T> void showTable(Dao<T> dao) {
        List<T> data = dao.getAll();
        EntityTable<T> table = new EntityTable<>(dao.getObjectClass(), data);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel()); // создаем TableRowSorter
        sorter.setComparator(0, (x, y) -> {
            int xInt = Integer.parseInt(x.toString());
            int yInt = Integer.parseInt(y.toString());

            return Integer.compare(xInt, yInt);
        });
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(); // создаем список SortKey для сортировки по первой колонке
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys); // устанавливаем список SortKey для TableRowSorter

        table.setRowSorter(sorter); // устанавливаем TableRowSorter на таблицу

        JScrollPane scrollPane = new JScrollPane(table);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(e -> {
            EditDialog<T> dialog = null;
            try {
                dialog = new EditDialog<>(this, dao.getObjectClass().newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                T insertedItem = dialog.getItem();
                dao.save(insertedItem);
                showTable(dao);
            }
        });

        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        updateButton.addActionListener(e -> {
            int selectedViewRow = table.getSelectedRow();
            if (selectedViewRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedViewRow);
                T selectedItem = data.get(modelRow);
                EditDialog<T> dialog = new EditDialog<>(this, selectedItem);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    T updatedItem = dialog.getItem();
                    dao.update(updatedItem);
                    showTable(dao);
                }
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedViewRow = table.getSelectedRow();
            if (selectedViewRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedViewRow);
                T selectedItem = data.get(modelRow);
                int option = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить запись?", "Удаление", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    dao.delete(selectedItem);
                    showTable(dao);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(insertButton);
        // buttonPanel.add(updateButton);
        // buttonPanel.add(deleteButton);
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            String tableName = (String) tableSelector.getSelectedItem();
            if (tableName != null) {
                Dao<?> dao = daoMap.get(tableName);
                if (dao != null) {
                    showTable(dao);
                }
            }
        }
    }
}