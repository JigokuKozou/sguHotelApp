package ru.shchelkin.util;

import ru.shchelkin.Frame.admin.AdminViewDataFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class EntityTable<T> extends JTable {
    private final DefaultTableModel tableModel;
    private List<T> data;
    private final Field[] fields;

    public EntityTable(Class<T> clazz, List<T> data) {
        this.data = data;
        this.fields = clazz.getDeclaredFields();
        tableModel = new EntityTableModel(fields, getRowsData());
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

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public class EntityTableModel extends DefaultTableModel {
        private final Field[] fields;

        public EntityTableModel(Field[] fields, Object[][] rowData) {
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
