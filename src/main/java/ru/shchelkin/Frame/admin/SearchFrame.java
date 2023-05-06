package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.model.Reservation;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchFrame extends BackButtonFrame {
    private final JLabel label = new JLabel("Id клиента:");
    private final JTextField textField = new JTextField(20);
    private final JButton button = new JButton("Поиск");

    private final JdbcTemplate jdbcTemplate;
    public SearchFrame(JdbcTemplate jdbcTemplate) {
        super("Активные бронирования");
        this.jdbcTemplate = jdbcTemplate;

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        Dimension preferredSize = textField.getPreferredSize();
        preferredSize.height = textField.getMinimumSize().height;
        textField.setMaximumSize(preferredSize);

        button.addActionListener(e -> {
            showTable();
        });

        leftPanel.add(label);
        leftPanel.add(textField);
        leftPanel.add(button);
    }

    private void showTable() {
        List<Reservation> data = getActiveReservationsForClient(Integer.parseInt(textField.getText()));
        AdminViewDataFrame.CustomTable<Reservation> table = new AdminViewDataFrame.CustomTable<>(Reservation.class, data);

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

    public List<Reservation> getActiveReservationsForClient(int clientId) {
        return jdbcTemplate.query(
                "SELECT * FROM Reservations WHERE id_client=" + clientId +
                        " AND end_booking > CURRENT_DATE",
                new BeanPropertyRowMapper<>(Reservation.class));
    }
}
