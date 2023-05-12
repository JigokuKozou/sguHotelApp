package ru.shchelkin.Frame.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shchelkin.Frame.BackButtonFrame;
import ru.shchelkin.model.Hotel;
import ru.shchelkin.util.EntityTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchHotelFrame extends BackButtonFrame {
    private final JLabel nameLabel = new JLabel("Название отеля:");
    private final JTextField nameText = new JTextField(20);
    private final JButton nameSearchButton = new JButton("Поиск");

    private final JLabel addressLabel = new JLabel("Адрес:");
    private final JTextField addressText = new JTextField(20);
    private final JButton addressButton = new JButton("Поиск");

    private final JdbcTemplate jdbcTemplate;

    public SearchHotelFrame(JdbcTemplate jdbcTemplate) {
        super("Поиск отеля");
        this.jdbcTemplate = jdbcTemplate;

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        int maxHeightText = nameText.getMinimumSize().height;
        Dimension maxSize = nameText.getPreferredSize();
        maxSize.height = maxHeightText;

        nameText.setMaximumSize(maxSize);
        addressText.setMaximumSize(maxSize);

        nameSearchButton.addActionListener(e -> {
            showTable(getHotelsByName(nameText.getText()));
        });

        addressButton.addActionListener(e -> {
            showTable(getHotelsByAddress(addressText.getText()));
        });

        leftPanel.add(nameLabel);
        leftPanel.add(nameText);
        leftPanel.add(nameSearchButton);

        JPanel passportPanel = new JPanel();
        passportPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        passportPanel.setLayout(new BoxLayout(passportPanel, BoxLayout.Y_AXIS));
        passportPanel.add(addressLabel);
        passportPanel.add(addressText);
        passportPanel.add(addressButton);

        leftPanel.add(passportPanel);
    }

    private void showTable(List<Hotel> data) {
        EntityTable<Hotel> table = new EntityTable<>(Hotel.class, data);

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

        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public java.util.List<Hotel> getHotelsByName(String searchableName) {
        return jdbcTemplate.query("SELECT * FROM get_hotels_by_name(?)",
                new BeanPropertyRowMapper<>(Hotel.class),
                searchableName);
    }

    public List<Hotel> getHotelsByAddress(String searchableAddress) {
        return jdbcTemplate.query("SELECT * FROM get_hotels_by_address(?)",
                new BeanPropertyRowMapper<>(Hotel.class),
                searchableAddress);
    }
}
