package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.dao.Dao;
import ru.shchelkin.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class AdminViewDataFrame extends BackButtonFrame {
    private JComboBox<String> tableSelector;

    private final Dao<Client> clientDao;
    private final Dao<Employee> employeeDao;
    private final Dao<FormerEmployee> formerEmployeeDao;
    private final Dao<Hotel> hotelDao;
    private final Dao<RoomType> roomTypeDao;
    private final Dao<Service> serviceDao;
    private final Dao<Room> roomDao;
    private final Dao<Cleaner> cleanerDao;
    private final Dao<Reservation> reservationDao;
    private final Dao<Accommodation> accommodationDao;
    private final Dao<OrderedService> orderedServiceDao;

    public AdminViewDataFrame(JdbcTemplate jdbcTemplate) {
        super("Просмотр справочников");

        clientDao = new Dao<>(Client.class, jdbcTemplate);
        employeeDao = new Dao<>(Employee.class, jdbcTemplate);
        formerEmployeeDao = new Dao<>(FormerEmployee.class, jdbcTemplate);
        hotelDao = new Dao<>(Hotel.class, jdbcTemplate);
        roomTypeDao = new Dao<>(RoomType.class, jdbcTemplate);
        serviceDao = new Dao<>(Service.class, jdbcTemplate);
        roomDao = new Dao<>(Room.class, jdbcTemplate);
        cleanerDao = new Dao<>(Cleaner.class, jdbcTemplate);
        reservationDao = new Dao<>(Reservation.class, jdbcTemplate);
        accommodationDao = new Dao<>(Accommodation.class, jdbcTemplate);
        orderedServiceDao = new Dao<>(OrderedService.class, jdbcTemplate);

        leftPanel.setLayout(new BorderLayout());

        tableSelector = new JComboBox<>(new String[]{
                "Client", "Employee", "FormerEmployee", "Hotel", "RoomType", "Service",
                "Room", "Cleaner", "Reservation", "Accommodation", "OrderedService"});
        tableSelector.addActionListener(e -> {
            String tableName = (String) tableSelector.getSelectedItem();
            if (tableName != null) {
                switch (tableName) {
                    case "Client":
                        showTable(Client.class, clientDao.getAll());
                        break;
                    case "Employee":
                        showTable(Employee.class, employeeDao.getAll());
                        break;
                    case "FormerEmployee":
                        showTable(FormerEmployee.class, formerEmployeeDao.getAll());
                        break;
                    case "Hotel":
                        showTable(Hotel.class, hotelDao.getAll());
                        break;
                    case "RoomType":
                        showTable(RoomType.class, roomTypeDao.getAll());
                        break;
                    case "Service":
                        showTable(Service.class, serviceDao.getAll());
                        break;
                    case "Room":
                        showTable(Room.class, roomDao.getAll());
                        break;
                    case "Cleaner":
                        showTable(Cleaner.class, cleanerDao.getAll());
                        break;
                    case "Reservation":
                        showTable(Reservation.class, reservationDao.getAll());
                        break;
                    case "Accommodation":
                        showTable(Accommodation.class, accommodationDao.getAll());
                        break;
                    case "OrderedService":
                        showTable(OrderedService.class, orderedServiceDao.getAll());
                        break;
                    default:
                        break;
                }
            }
        });
        leftPanel.add(tableSelector, BorderLayout.NORTH);
    }

    private <T> void showTable(Class<T> clazz, List<T> data) {
        CustomTable<T> table = new CustomTable<>(clazz, data);
        JScrollPane scrollPane = new JScrollPane(table,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static class CustomTable<T> extends JTable {
        private final DefaultTableModel tableModel;
        private final List<T> data;
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