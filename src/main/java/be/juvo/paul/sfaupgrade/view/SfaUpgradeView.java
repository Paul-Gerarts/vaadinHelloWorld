package be.juvo.paul.sfaupgrade.view;

import be.juvo.paul.sfaupgrade.model.Customer;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SfaUpgradeView extends VerticalLayout {

    @Autowired
    public SfaUpgradeView() {
        ComboBox<Customer> comboBox = new ComboBox<>("Customers");
        comboBox.setDataProvider(getCustomerDataProvider(Customer.getDummyCustomers()));
        comboBox.setItemLabelGenerator(Customer::getName);

        add(comboBox);

    }

    // creates a comboBox with autosuggestion filters
    private CallbackDataProvider<Customer, String> getCustomerDataProvider(List<Customer> customers) {
        return DataProvider.fromFilteringCallbacks(query -> {
            String filter = query.getFilter().orElse("");
            int offSet = query.getOffset();
            int limit = query.getLimit();
            return customers.stream()
                    .filter(customer -> {
                        return customer.getName().contains(filter)
                                || String.valueOf(customer.getInternalNumber()).contains(filter)
                                || String.valueOf(customer.isConsultant()).contains(filter);
                    });
        }, query -> {
            String filter = query.getFilter().orElse("");
            return Long.valueOf(customers.stream()
                    .filter(customer -> {
                        return customer.getName().contains(filter)
                                || String.valueOf(customer.getInternalNumber()).contains(filter)
                                || String.valueOf(customer.isConsultant()).contains(filter);
                    }).count()).intValue();
        });
    }


}
