package be.juvo.paul.formpractice.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ProductForm extends VerticalLayout {

    public ProductForm() {
        add(new TextField("Name"));

        add(new TextArea(" Description"));

        NumberField price = new NumberField("Price");
        price.setSuffixComponent(new Span("€"));
        price.setStep(0.01);
        add(price);

        add(new DatePicker("Available"));

        // combination of listBox and textField, in which you can type for a value to select it
        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems("A", "B", "C");
        category.setPreventInvalidInput(true);
        add(category);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Cancel");
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        add(buttons);

        NumberField dollarField = new NumberField("Dollars");
        dollarField.setPrefixComponent(new Span("$"));
        dollarField.addKeyPressListener(event -> {
            if (event.getKey() == Key.ENTER) {
                log.info("fieldValue = {}", dollarField.getValue());
            }
        });

        NumberField euroField = new NumberField("Euros");
        euroField.setSuffixComponent(new Span("€"));
        euroField.addKeyPressListener(event -> {
            if (event.getKey() == Key.ENTER) {
                log.info("fieldValue = {}", dollarField.getValue());
            }
        });

        NumberField stepperField = new NumberField("Stepper");
        stepperField.setValue(1d);
        stepperField.setMin(0);
        stepperField.setMax(10);
        stepperField.setHasControls(true);
        stepperField.addValueChangeListener(event -> log.info("fieldValue = {}", dollarField.getValue()));

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");

        HorizontalLayout fields = new HorizontalLayout(dollarField, euroField, stepperField, passwordField);
        add(fields);

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("My CHECKBOX");

        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        group.setItems("foo", "bar", "fooBar");
        group.addValueChangeListener(event -> log.info("Value changed from '{}' to '{}'", event.getOldValue(), event.getValue()));

        // selects a single option
        ListBox<String> listBox = new ListBox<>();
        listBox.setItems("this", "is", "a", "listBox");

        Select<String> select = new Select<>("one", "two");
        select.setPlaceholder("Select-option");
        select.setLabel("select");

        HorizontalLayout selects = new HorizontalLayout(checkbox, group, listBox, select);
        add(selects);

        DatePicker datePicker = new DatePicker();
        datePicker.setLabel("Select a day within this month");
        datePicker.setPlaceholder("Date within this month");
        LocalDate now = LocalDate.now();
        datePicker.setMin(now.withDayOfMonth(1));
        datePicker.setMax(now.withDayOfMonth(now.lengthOfMonth()));

        TimePicker timePicker = new TimePicker();

        HorizontalLayout pickers = new HorizontalLayout(datePicker, timePicker);
        add(pickers);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            log.info("uploaded something?");
            // TODO figure the createComponent out
            // Component component = createComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream());
            // showOutput(event.getFileName(), component, output);
        });
    }
}
