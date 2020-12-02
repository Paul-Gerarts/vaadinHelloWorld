package be.juvo.paul.formpractice.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

@Tag("custom-accordion")
@Getter
@Setter
public class CustomAccordion extends Component {

    private Accordion accordion = new Accordion();

    /*
     * similar would be the Details component, but that offers only 1 panel instead of multiple like the accordion
     */
    public CustomAccordion() {

        VerticalLayout personalInformationLayout = new VerticalLayout();
        personalInformationLayout.add(
                new TextField("Name"),
                new TextField("Phone"),
                new TextField("Email")
        );

        accordion.add("Personal Information", personalInformationLayout);

        VerticalLayout billingAddressLayout = new VerticalLayout();
        billingAddressLayout.add(
                new TextField("Address"),
                new TextField("City"),
                new TextField("State"),
                new TextField("Zip Code")
        );
        accordion.add("Billing Address", billingAddressLayout);

        VerticalLayout paymenLayout = new VerticalLayout();
        paymenLayout.add(
                new Span("Not yet implemented")
        );
        AccordionPanel billingAddressPanel = accordion.add("Payment", paymenLayout);
        billingAddressPanel.setEnabled(false);
    }
}
