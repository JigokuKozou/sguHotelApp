package ru.shchelkin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.shchelkin.Frame.OperationModeFrame;
import ru.shchelkin.Frame.admin.AdminMenu;
import ru.shchelkin.Frame.admin.AdminViewDataFrame;
import ru.shchelkin.Frame.ReportFrame;
import ru.shchelkin.Frame.admin.SearchClientFrame;
import ru.shchelkin.Frame.user.SearchHotelFrame;
import ru.shchelkin.Frame.user.UserMenu;
import ru.shchelkin.Frame.user.UserViewDataFrame;
import ru.shchelkin.model.EmployeeCleanerDates;
import ru.shchelkin.model.HotelsInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Program {

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/HotelDB");
        dataSource.setUsername("postgres");
        dataSource.setPassword("rootroot");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        Map<String, Class<?>> adminViews = new LinkedHashMap<>();
        adminViews.put("HotelsInfo", HotelsInfo.class);
        adminViews.put("EmployeeCleanerDates", EmployeeCleanerDates.class);

        Map<String, Class<?>> userViews = new LinkedHashMap<>();
        userViews.put("HotelsInfo", HotelsInfo.class);

        var adminViewData = new AdminViewDataFrame(jdbcTemplate);
        var reportFrame = new ReportFrame(jdbcTemplate, adminViews);
        var searchFrame = new SearchClientFrame(jdbcTemplate);
        var adminMenu = new AdminMenu(adminViewData, reportFrame, searchFrame);
        adminViewData.setParentFrame(adminMenu);
        reportFrame.setParentFrame(adminMenu);
        searchFrame.setParentFrame(adminMenu);


        var userViewData = new UserViewDataFrame(jdbcTemplate);
        var userReportFrame = new ReportFrame(jdbcTemplate, userViews);
        var hotelSearchFrame = new SearchHotelFrame(jdbcTemplate);
        var userMenu = new UserMenu(userViewData, userReportFrame, hotelSearchFrame);
        userViewData.setParentFrame(userMenu);
        userReportFrame.setParentFrame(userMenu);
        hotelSearchFrame.setParentFrame(userMenu);

        var operationMode = new OperationModeFrame(adminMenu, userMenu);
        adminMenu.setParentFrame(operationMode);
        userMenu.setParentFrame(operationMode);

        operationMode.setVisible(true);
    }
}