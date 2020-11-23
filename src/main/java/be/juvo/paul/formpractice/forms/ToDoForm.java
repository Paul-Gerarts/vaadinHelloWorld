package be.juvo.paul.formpractice.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ToDoForm extends VerticalLayout {

    public ToDoForm() {
        VerticalLayout todoList = new VerticalLayout();
        TextField taskField = new TextField();
        Button addButton = new Button("Add");
        // commented the next line out because of multiple clickShortCuts - this one threw exception because the underlaying save to the database (restraint-validation)
        // addButton.addClickShortcut(Key.ENTER);
        addButton.addClickListener(click -> {
            Checkbox checkbox = new Checkbox(taskField.getValue());
            todoList.add(checkbox);
        });
        add(
                new H1("Vaadin Todo"),
                todoList,
                new HorizontalLayout(
                        taskField,
                        addButton
                )
        );
    }
}
