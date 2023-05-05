package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.dao.Dao;
import ru.shchelkin.model.*;
import ru.shchelkin.util.EditDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminViewDataFrame extends BackButtonFrame {
    private final Map<String, Dao<?>> daoMap = new LinkedHashMap<>();
    private JComboBox<String> tableSelector;

    public AdminViewDataFrame(JdbcTemplate jdbcTemplate) {
        super("Просмотр справочников");

        daoMap.put("Client", new Dao<>(Client.class, jdbcTemplate));
        daoMap.put("Employee", new Dao<>(Employee.class, jdbcTemplate));
        daoMap.put("Hotel", new Dao<>(Hotel.class, jdbcTemplate));
        daoMap.put("RoomType", new Dao<>(RoomType.class, jdbcTemplate));
        daoMap.put("Service", new Dao<>(Service.class, jdbcTemplate));
        daoMap.put("Room", new Dao<>(Room.class, jdbcTemplate));
        daoMap.put("Cleaner", new Dao<>(Cleaner.class, jdbcTemplate));
        daoMap.put("Reservation", new Dao<>(Reservation.class, jdbcTemplate));
        daoMap.put("Accommodation", new Dao<>(Accommodation.class, jdbcTemplate));
        daoMap.put("OrderedService", new Dao<>(OrderedService.class, jdbcTemplate));

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
        CustomTable<T> table = new CustomTable<>(dao.getObjectClass(), data);
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                T selectedItem = data.get(selectedRow);
                EditDialog<T> dialog = new EditDialog<>(this, selectedItem);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    T updatedItem = dialog.getItem();
                    // Изменить данные выбранной строки на основе данных из формы редактирования
                    dao.update(updatedItem);
                    // Обновить данные в таблице
                    List<T> newData = dao.getAll();
                    table.setData(newData);
                    table.fireTableDataChanged();
                }
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                T selectedItem = data.get(selectedRow);
                int option = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить запись?", "Удаление", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Удалить выбранную строку из таблицы
                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.removeRow(selectedRow);
                    // Удалить выбранную строку из БД
                    dao.delete(selectedItem);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static class CustomTable<T> extends JTable {
        private final DefaultTableModel tableModel;
        private List<T> data;
        private final Field[] fields;

        public CustomTable(Class<T> clazz, List<T> data) {
            this.data = data;
            this.fields = clazz.getDeclaredFields();
            tableModel = new CustomTableModel(fields, getDataArray());
            setModel(tableModel);
            setAutoCreateRowSorter(true);
            setFillsViewportHeight(true);
        }

        private Object[][] getDataArray() {
            Object[][] array = new Object[data.size()][fields.length];
            for (int i = 0; i < data.size(); i++) {
                T obj = data.get(i);
                for (int j = 0; j < fields.length; j++) {
                    try {
                        fields[j].setAccessible(true);
                        Object value = fields[j].get(obj);
                        if (value instanceof String && ((String) value).matches("\\d+,*\\d*")) {
                            value = ((String) value).replace(",", ".");
                        }
                        array[i][j] = value;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return array;
        }

        public void setData(List<T> newData) {
            this.data = newData;
            DefaultTableModel model = (DefaultTableModel) this.getModel();
            model.setRowCount(0);
            for (T item : newData) {
                model.addRow(getRowData(item));
            }
        }

        private Object[] getRowData(T item) {
            Object[] rowData = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    Object value = fields[i].get(item);
                    if (value instanceof String && ((String) value).matches("\\d+,*\\d*")) {
                        value = ((String) value).replace(",", ".");
                    }
                    rowData[i] = value;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return rowData;
        }

        private Object prepareCellValue(Object value) {
            if (value instanceof String && ((String) value).matches("\\d+,*\\d*")) {
                value = Double.parseDouble(((String) value).replace(",", "."));
            }
            return value;
        }

        public void fireTableDataChanged() {
            tableModel.fireTableStructureChanged();
            tableModel.fireTableRowsUpdated(0, data.size() - 1);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        private class CustomTableModel extends DefaultTableModel {
            private final Field[] fields;

            public CustomTableModel(Field[] fields, Object[][] rowData) {
                super(rowData, getColumnNames(fields));
                this.fields = fields;
            }

            private static String[] getColumnNames(Field[] fields) {
                String[] columnNames = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    columnNames[i] = fields[i].getName();
                }
                return columnNames;
            }
        }
    }
}