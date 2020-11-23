package be.juvo.paul.formpractice.customfields;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
public class CustomDateTimePicker extends CustomField<LocalDateTime> {

    private final DatePicker datePicker = new DatePicker();
    private final TimePicker timePicker = new TimePicker();
    private final Button showDateTime = new Button();

    public CustomDateTimePicker() {
        this.showDateTime.setText("Show dateTime");
        this.showDateTime.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.showDateTime.addClickListener(event -> log.info(generateModelValue().toString()));
        setLabel("Start datetime");
        add(datePicker, timePicker, showDateTime);
    }

    @Override
    public LocalDateTime generateModelValue() {
        final LocalDate date = datePicker.getValue();
        final LocalTime time = timePicker.getValue();
        return date != null && time != null
                ? LocalDateTime.of(date, time)
                : null;
    }

    @Override
    public void setPresentationValue(LocalDateTime newPresentationValue) {
        datePicker.setValue(newPresentationValue != null
                ? newPresentationValue.toLocalDate()
                : null);
        timePicker.setValue(newPresentationValue != null
                ? newPresentationValue.toLocalTime()
                : null);
    }
}
