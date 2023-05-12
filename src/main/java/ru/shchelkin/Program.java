package ru.shchelkin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.shchelkin.Frame.OperationModeFrame;
import ru.shchelkin.Frame.admin.AdminMenu;
import ru.shchelkin.Frame.admin.AdminViewDataFrame;
import ru.shchelkin.Frame.admin.ReportFrame;
import ru.shchelkin.Frame.admin.SearchClientFrame;
import ru.shchelkin.Frame.user.UserMenu;
import ru.shchelkin.Frame.user.UserViewDataFrame;

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
        var reportFrame = new ReportFrame(jdbcTemplate);
        var searchFrame = new SearchClientFrame(jdbcTemplate);
        var adminMenu = new AdminMenu(adminViewData, reportFrame, searchFrame);
        adminViewData.setParentFrame(adminMenu);
        reportFrame.setParentFrame(adminMenu);
        searchFrame.setParentFrame(adminMenu);


        var userViewData = new UserViewDataFrame(jdbcTemplate);
        var userReportFrame = new ReportFrame(jdbcTemplate);
        var userSearchFrame = new SearchClientFrame(jdbcTemplate);
        var userMenu = new UserMenu(userViewData, userReportFrame, userSearchFrame);
        userViewData.setParentFrame(userMenu);
        userReportFrame.setParentFrame(userMenu);
        userSearchFrame.setParentFrame(userMenu);

        var operationMode = new OperationModeFrame(adminMenu, userMenu);
        adminMenu.setParentFrame(operationMode);
        userMenu.setParentFrame(operationMode);

        operationMode.setVisible(true);
    }
}