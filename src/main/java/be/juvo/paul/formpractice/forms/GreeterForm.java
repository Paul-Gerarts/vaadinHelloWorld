package be.juvo.paul.formpractice.forms;

import be.juvo.paul.formpractice.GreetService;
import be.juvo.paul.formpractice.services.UserServiceImpl;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreeterForm extends VerticalLayout {

    private UserServiceImpl userService;
    private GreetService greetService;

    /*
     * Extracting this from MainView to seperate class,
     * caused the next Form in MainView to move position.
     * Find out why.
     */
    @Autowired
    public GreeterForm(
            GreetService service,
            UserServiceImpl userService
    ) {
        this.greetService = service;
        this.userService = userService;
        // Use TextField for standard text input
        TextField textField = new TextField("Your name");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> {
                    //Notification.show(service.greet(textField.getValue()));
                    userService.saveUser(textField.getValue());
                    System.out.println(userService.findAll());  // database works, but tables don't show
                });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button); // adds the component to the VerticalLayout
    }
}
