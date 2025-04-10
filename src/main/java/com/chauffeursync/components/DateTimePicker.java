package com.chauffeursync.components;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateTimePicker extends VBox {

    private final DatePicker datePicker;
    private final Spinner<LocalTime> timeSpinner;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DateTimePicker() {
        this.datePicker = new DatePicker();
        this.timeSpinner = createTimeSpinner();

        Label dateLabel = new Label("Datum:");
        Label timeLabel = new Label("Tijd:");

        HBox timeBox = new HBox(10, timeLabel, timeSpinner);
        HBox dateBox = new HBox(10, dateLabel, datePicker);
        dateBox.setPadding(new Insets(5));
        timeBox.setPadding(new Insets(5));

        this.getChildren().addAll(dateBox, timeBox);
        this.setSpacing(5);
    }

    private Spinner<LocalTime> createTimeSpinner() {
        var times = IntStream.range(0, 24 * 4)
                .mapToObj(i -> LocalTime.of(i / 4, (i % 4) * 15))
                .collect(Collectors.toList());

        Spinner<LocalTime> spinner = new Spinner<>();
        var valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(
                FXCollections.observableArrayList(times)
        );

        valueFactory.setConverter(new StringConverter<>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime time) {
                return time != null ? time.format(formatter) : "";
            }

            @Override
            public LocalTime fromString(String string) {
                try {
                    return LocalTime.parse(string, formatter);
                } catch (Exception e) {
                    System.err.println("Ongeldige tijdinvoer: " + string);
                    return valueFactory.getValue();
                }
            }
        });

        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);
        valueFactory.setValue(LocalTime.of(8, 0)); // default

        return spinner;
    }


    public LocalDateTime getDateTime() {
        LocalDate date = datePicker.getValue();
        LocalTime time = timeSpinner.getValue();
        if (date != null && time != null) {
            return LocalDateTime.of(date, time);
        }
        return null;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            datePicker.setValue(dateTime.toLocalDate());
            timeSpinner.getValueFactory().setValue(dateTime.toLocalTime());
        }
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public Spinner<LocalTime> getTimeSpinner() {
        return timeSpinner;
    }
    public String getDateTimeAsString() {
        LocalDateTime dt = getDateTime();
        return dt != null ? dt.format(FORMATTER) : "";
    }

    public void setDateTimeFromString(String dateTimeString) {
        try {
            LocalDateTime parsed = LocalDateTime.parse(dateTimeString, FORMATTER);
            setDateTime(parsed);
        } catch (DateTimeParseException e) {
            System.err.println("Ongeldige datum/tijd: " + dateTimeString);
        }
    }
}
