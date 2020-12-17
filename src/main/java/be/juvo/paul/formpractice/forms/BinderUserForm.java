package be.juvo.paul.formpractice.forms;

import be.juvo.paul.formpractice.entities.User;
import be.juvo.paul.formpractice.services.UserServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BinderUserForm extends VerticalLayout {

    private UserServiceImpl userService;

    @Autowired
    public BinderUserForm(UserServiceImpl userService) {
        this.userService = userService;

        // create User to read into Binder
        User dummyUser = userService.createDummyUser();
        userService.save(dummyUser);

        // create Binder
        Binder<User> binder = new Binder<>(User.class);

        TextField functionField = new TextField();
        Label functionStatus = new Label();
        functionStatus.getStyle().set("color", "red");
        functionField.setLabel(functionStatus.getText());

        // bind class-variable: option 1
        binder.forField(functionField)
                .asRequired("Every employee must have a function") // shorthand validator implicating the field MUST be filled in
                .withValidator(function -> function.length() > 3, "We don't have short functions") // validator is optional
                .withStatusLabel(functionStatus)
                //.withConverter(new StringToIntegerConverter("Not a number")) // it's possible to convert in a binding, with its own validator worked in
                .bind(
                        User::getFunction,
                        User::setFunction // to disallow setting new input (making it read-only), bind the setter by 'null'
                );

        TextField firstNameField = new TextField();

        // bind class-variable: option 2
        binder.bind(firstNameField, User::getFirstName, User::setFirstName);

        TextField lastNameField = new TextField();
        Label lastNameStatus = new Label();
        lastNameStatus.getStyle().set("color", "red");
        lastNameField.setLabel(lastNameStatus.getText());

        binder.forField(lastNameField)
                .withValidator(lastName -> lastName.length() > 3, "We don't do short names here") // does prevent a save to repository
                .withValidator(lastName -> !lastName.contains("Cabbage"), "Cabbages aren't allowed to work here, sorry") // multiple validators allowed
                .withValidationStatusHandler(status -> {
                    lastNameStatus.setText(status.getMessage().orElse(""));
                    lastNameStatus.setVisible(status.isError());
                })
                .bind(User::getLastName, User::setLastName);

        // allow binder to read User
        binder.readBean(dummyUser);

        Button saveButton = new Button("Save user", clickEvent -> {
            try {
                // takes the input value and saves it
                binder.writeBean(dummyUser);
                userService.save(dummyUser);
            } catch (ValidationException ve) {
                log.error("you fucked up binding the Bean to the Binder. ", ve);
            }
        });

        Button resetButton = new Button("Reset", event -> binder.readBean(dummyUser));

        add(functionField, functionStatus, firstNameField, lastNameField, lastNameStatus, saveButton, resetButton);

      /*// Triggering revalidation when another field changes
        Binder<Trip> binder = new Binder<>(Trip.class);
        DatePicker departing = new DatePicker();
        departing.setLabel("Departing");
        DatePicker returning = new DatePicker();
        returning.setLabel("Returning");
        // Store return date binding so we can
        // revalidate it later
        Binder.Binding<Trip, LocalDate> returningBinding = binder.forField(returning)
                .withValidator(returnDate ->
                        !returnDate.isBefore(departing.getValue()), "Cannot return before departing")
                .bind(Trip::getReturnDate, Trip::setReturnDate);
        // Revalidate return date when departure date changes
        departing.addValueChangeListener(event -> returningBinding.validate());*/

        /*// example with multiple validators + converter
        binder.forField(yearOfBirthField)
                // Validator will be run with the String value
                // of the field
                .withValidator(text -> text.length() == 4, "Doesn't look like a year")
                // Converter will only be run for strings
                // with 4 characters
                .withConverter(new StringToIntegerConverter("Must enter a number"))
                // Validator will be run with the converted value
                .withValidator(year -> year >= 1900 && year < 2000, "Person must be born in the 20th century")
                .bind(Person::getYearOfBirth, Person::setYearOfBirth);*/
    }
}
