package be.juvo.paul;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ProductForm extends VerticalLayout {

    public ProductForm() {
        add(new TextField("Name"));

        add(new TextArea(" Description"));

        NumberField price = new NumberField("Price");
        price.setSuffixComponent(new Span("€"));
        price.setStep(0.01);
        add(price);

        add(new DatePicker("Available"));

        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems("A", "B", "C");
        add(category);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Cancel");
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        add(buttons);
    }
}
