package ru.shchelkin.Frame.admin;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.model.Client;
import ru.shchelkin.util.EntityTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchClientFrame extends BackButtonFrame {
    private final JLabel surnameLabel = new JLabel("Фамилия:");
    private final JTextField surnameText = new JTextField(20);
    private final JButton surnameSearchButton = new JButton("Поиск");

    private final JLabel passportSerialLabel = new JLabel("Серия:");
    private final JTextField passportSerialText = new JTextField(20);
    private final JLabel passportNumberLabel = new JLabel("Номер:");
    private final JTextField passportNumberText = new JTextField(20);
    private final JButton passportSearchButton = new JButton("Поиск");

    private final JdbcTemplate jdbcTemplate;

    public SearchClientFrame(JdbcTemplate jdbcTemplate) {
        super("Поиск клиента");
        this.jdbcTemplate = jdbcTemplate;

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        int maxHeightText = surnameText.getMinimumSize().height;
        Dimension maxSize = surnameText.getPreferredSize();
        maxSize.height = maxHeightText;

        surnameText.setMaximumSize(maxSize);
        passportSerialText.setMaximumSize(maxSize);
        passportNumberText.setMaximumSize(maxSize);

        surnameSearchButton.addActionListener(e -> {
            showTable(getClientBySurnameStartWith(surnameText.getText()));
        });

        passportSearchButton.addActionListener(e -> {
            int passportSerial = Integer.parseInt(passportSerialText.getText());
            int passportNumber = Integer.parseInt(passportNumberText.getText());
            showTable(getClientByPassport(passportSerial, passportNumber));
        });

        leftPanel.add(surnameLabel);
        leftPanel.add(surnameText);
        leftPanel.add(surnameSearchButton);

        JPanel passportPanel = new JPanel();
        passportPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        passportPanel.setLayout(new BoxLayout(passportPanel, BoxLayout.Y_AXIS));
        passportPanel.add(passportSerialLabel);
        passportPanel.add(passportSerialText);
        passportPanel.add(passportNumberLabel);
        passportPanel.add(passportNumberText);
        passportPanel.add(passportSearchButton);

        leftPanel.add(passportPanel);
    }

    private void showTable(List<Client> data) {
        EntityTable<Client> table = new EntityTable<>(Client.class, data);

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

    public List<Client> getClientBySurnameStartWith(String startSurname) {
        return jdbcTemplate.query("SELECT * FROM get_clients_by_surname(?)",
                new BeanPropertyRowMapper<>(Client.class),
                startSurname);
    }

    public List<Client> getClientByPassport(int passport_serial, int passport_number) {
        return jdbcTemplate.query("SELECT * FROM get_clients_by_passport(?,?)",
                new BeanPropertyRowMapper<>(Client.class),
                passport_serial, passport_number);
    }
}
