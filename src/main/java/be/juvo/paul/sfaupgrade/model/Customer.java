package be.juvo.paul.sfaupgrade.model;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Customer {

    private String name;
    private int internalNumber;
    private boolean consultant;

    public static List<Customer> getDummyCustomers() {
        Customer one = new Customer("Paul Gerarts", 1000, true);
        Customer two = new Customer("Davy Dewolf", 2345, false);
        Customer three = new Customer("Mark Marissen", 23888, false);
        return Arrays.asList(one, two, three);
    }
}
