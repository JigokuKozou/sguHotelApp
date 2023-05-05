package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.dao.Dao;
import ru.shchelkin.model.*;
import ru.shchelkin.util.EditDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

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

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel()); // создаем TableRowSorter

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(); // создаем список SortKey для сортировки по первой колонке
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys); // устанавливаем список SortKey для TableRowSorter

        table.setRowSorter(sorter); // устанавливаем TableRowSorter на таблицу

        JScrollPane scrollPane = new JScrollPane(table);

        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        updateButton.addActionListener(e -> {
            int selectedViewRow  = table.getSelectedRow();
            if (selectedViewRow  != -1) {
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
            tableModel = new CustomTableModel(fields, getRowsData());
            setModel(tableModel);
            setAutoCreateRowSorter(true);
            setFillsViewportHeight(true);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        public void setData(List<T> newData) {
            this.data = newData;
            tableModel.setRowCount(0);
            for (T item : newData) {
                tableModel.addRow(getRowData(item));
            }
        }

        private Object prepareCellValue(Object value) {
            if (value instanceof String && ((String) value).matches("\\d+,*\\d*")) {
                value = Double.parseDouble(((String) value).replace(",", "."));
            }
            return value;
        }

        private Object[] getRowData(T item) {
            Object[] rowData = new Object[fields.length];
            IntStream.range(0, fields.length)
                    .forEach(i -> {
                        fields[i].setAccessible(true);
                        try {
                            rowData[i] = prepareCellValue(fields[i].get(item));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
            return rowData;
        }

        private Object[][] getRowsData() {
            return data.stream()
                    .map(this::getRowData)
                    .toArray(Object[][]::new);
        }

        public void fireTableDataChanged() {
            tableModel.fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public static class CustomTableModel extends DefaultTableModel {
            private final Field[] fields;

            public CustomTableModel(Field[] fields, Object[][] rowData) {
                super(rowData, getColumnNames(fields));
                this.fields = fields;
            }

            private static String[] getColumnNames(Field[] fields) {
                return Arrays.stream(fields)
                        .map(Field::getName)
                        .toArray(String[]::new);
            }
        }
    }
}