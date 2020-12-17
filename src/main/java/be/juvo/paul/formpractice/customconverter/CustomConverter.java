package be.juvo.paul.formpractice.customconverter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class CustomConverter implements Converter<String, Integer> {

    /*
     * Translates as: only change the logic in the return statements
     */
    @Override
    public Result<Integer> convertToModel(String fieldValue, ValueContext context) {
        // Produces a converted value or an error
        try {
            // ok is a static helper method that creates a Result
            return Result.ok(Integer.valueOf(fieldValue));
        } catch (NumberFormatException e) {
            // error is a static helper method that creates a Result
            return Result.error("Enter a number");
        }
    }

    /*
     * Translates as: never change this method
     */
    @Override
    public String convertToPresentation(Integer integer, ValueContext context) {
        // Converting to the field type should always succeed, so there is no support for returning an error Result.
        return String.valueOf(integer);
    }

  /*// Using the converter
    binder.forField(yearOfBirthField)
            .withConverter(new CustomConverter())
            .bind(Person::getYearOfBirth, Person::setYearOfBirth);*/
}
