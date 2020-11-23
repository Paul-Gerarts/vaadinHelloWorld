package be.juvo.paul.formpractice.forms;

import be.juvo.paul.formpractice.GreetService;
import be.juvo.paul.formpractice.customcomponents.MyLabel;
import be.juvo.paul.formpractice.services.UserServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
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

        // check usage from "creating components"
        MyLabel myLabel = new MyLabel();
        myLabel.setText("custom label");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> {
                    Notification.show(service.showName(textField.getValue())).setPosition(Notification.Position.BOTTOM_END);
                    userService.saveUser(textField.getValue());
                    userService.findAll().forEach(user -> {
                        log.info("userName: {}", user.getFirstName());  // database works, but tables don't show
                    });
                });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        // button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button, myLabel); // adds the component to the VerticalLayout
    }
}
