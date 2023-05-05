package ru.shchelkin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.shchelkin.Frame.admin.AdminMenu;
import ru.shchelkin.Frame.admin.AdminViewDataFrame;
import ru.shchelkin.Frame.OperationModeFrame;
import ru.shchelkin.Frame.UserMenuFrame;

public class Program {

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/HotelDB");
        dataSource.setUsername("postgres");
        dataSource.setPassword("rootroot");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        var adminViewData = new AdminViewDataFrame(jdbcTemplate);
        var adminMenu = new AdminMenu(adminViewData);
        adminViewData.setParentFrame(adminMenu);

        var userMenu = new UserMenuFrame();

        var operationMode = new OperationModeFrame(adminMenu, userMenu);
        adminMenu.setParentFrame(operationMode);
        userMenu.setParentFrame(operationMode);

        operationMode.setVisible(true);
    }
}