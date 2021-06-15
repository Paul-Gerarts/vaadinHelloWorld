package be.juvo.paul.formpractice.forms;

import be.juvo.paul.formpractice.customcomponents.CustomAccordion;
import be.juvo.paul.formpractice.customfields.CustomDateTimePicker;
import be.juvo.paul.sfaupgrade.model.Customer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
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
        ComboBox<Customer> category = new ComboBox<>("Customers");
        category.setItems(Customer.getDummyCustomers());
        category.setPreventInvalidInput(true);
        category.setItemLabelGenerator(Customer::getName);
        add(category);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Cancel");
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        add(buttons);

        NumberField dollarField = new NumberField("Dollars");
        dollarField.setPrefixComponent(new Span("$"));
        dollarField.addValueChangeListener(event -> log.info("dollerFieldValue = {}", dollarField.getValue()));

        NumberField euroField = new NumberField("Euros");
        euroField.setSuffixComponent(new Span("€"));
        euroField.addValueChangeListener(event -> log.info("euroFieldValue = {}", euroField.getValue()));

        NumberField stepperField = new NumberField("Stepper");
        stepperField.setValue(1d);
        stepperField.setMin(0);
        stepperField.setMax(10);
        stepperField.setHasControls(true);
        stepperField.addValueChangeListener(event -> log.info("stepperFieldValue = {}", stepperField.getValue()));

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");

        HorizontalLayout fields = new HorizontalLayout(dollarField, euroField, stepperField, passwordField);
        add(fields);

        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("My CHECKBOX");
        checkbox.setWidth("200px");

        // how to style these (width, height... flex?)
        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        group.setItems("foo", "bar", "fooBar");
        group.addValueChangeListener(event -> log.info("Value changed from '{}' to '{}'", event.getOldValue(), event.getValue()));

        // selects a single option
        ListBox<String> listBox = new ListBox<>();
        listBox.setItems("this", "is", "a", "listBox");
        // de-selecting a value possible? Keeps showing the checkMark once a value is selected

        Select<String> select = new Select<>("", "one", "two");
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

        HorizontalLayout customDateTimePicker = new HorizontalLayout(new CustomDateTimePicker());
        add(customDateTimePicker);

        CustomAccordion customAccordion = new CustomAccordion();
        HorizontalLayout accordion = new HorizontalLayout(customAccordion.getAccordion());
        add(accordion);

        Dialog popUp = new Dialog();
        popUp.add(new Label("Close me with the esc-key or an outside click"));
        popUp.setWidth("400px");
        popUp.setHeight("150px");
        save.addClickListener(event -> popUp.open());

        // programmatically update the value
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.345);
        add(progressBar);

        Tab tab1 = new Tab("Tab one");
        Tab tab2 = new Tab("Tab two");
        Tab tab3 = new Tab("Tab three");
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        add(tabs);

        // two options of initializing
        Icon edit = new Icon(VaadinIcon.EDIT);
        Icon close = VaadinIcon.CLOSE.create();
        add(edit, close);

        LoginForm component = new LoginForm();
        component.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e);
            if (isAuthenticated) {
                // do nothing
            } else {
                component.setError(true);
            }
        });

    }

    private boolean authenticate(AbstractLogin.LoginEvent event) {
        return event.getPassword().equalsIgnoreCase("password") && event.getUsername().equalsIgnoreCase("username");
    }
}
