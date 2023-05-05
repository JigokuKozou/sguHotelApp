package ru.shchelkin.util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditDialog<T> extends JDialog {

    private JPanel contentPanel;
    private JButton okButton;
    private JButton cancelButton;
    private T item;
    private boolean isConfirmed;

    public EditDialog(Frame owner, T item) {
        super(owner, "Редактирование", true);
        setSize(400, 300);
        setLocationRelativeTo(null);

        this.item = item;
        // Получаем поля объекта с использованием рефлексии
        Field[] fields = item.getClass().getDeclaredFields();

        // Создаем панель контента и добавляем на нее поля для редактирования данных
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(fields.length + 1, 2));
        for (Field field : fields) {
            JLabel label = new JLabel(field.getName(), JLabel.RIGHT);
            JTextField textField = new JTextField();
            if (field.getName().equals("id")) // если имя поля "id"
                textField.setEditable(false);
            try {
                field.setAccessible(true); // Позволяет получать доступ к приватным полям
                Object value = field.get(item);
                textField.setText(value != null ? value.toString() : ""); // значение, которое может быть null, проверяем на null и преобразуем в пустую строку
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            contentPanel.add(label);
            contentPanel.add(textField);
        }

        // Создаем кнопки OK и Отмена
        okButton = new JButton("OK");
        cancelButton = new JButton("Отмена");
        okButton.addActionListener(e -> {
            isConfirmed = true;
            // Создаем объект с обновленными данными
            try {
                T updatedItem = (T) item.getClass().getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    JTextField textField = (JTextField) contentPanel.getComponent(ArrayUtils.indexOf(fields, field) * 2 + 1);
                    // Обрабатываем пустое значение и формат даты/времени
                    Object value = null;
                    if (!textField.getText().isEmpty()) {
                        if (field.getType().equals(LocalDateTime.class)) {
                            value = LocalDateTime.parse(textField.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        } else {
                            value = ConvertUtils.convert(textField.getText(), field.getType());
                        }
                    }
                    field.set(updatedItem, value); // Установка обновленных данных выбранной строки
                }
                this.item = updatedItem;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dispose();
        });
        cancelButton.addActionListener(e -> {
            isConfirmed = false;
            dispose();
        });

        // Добавляем кнопки на панель контента
        contentPanel.add(okButton);
        contentPanel.add(cancelButton);

        setContentPane(contentPanel);
        getRootPane().setDefaultButton(okButton);
        pack();
    }

    public T getItem() {
        return item;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
