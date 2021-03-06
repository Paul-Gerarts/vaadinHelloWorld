package be.juvo.paul.formpractice;

import be.juvo.paul.formpractice.forms.BinderUserForm;
import be.juvo.paul.formpractice.forms.GreeterForm;
import be.juvo.paul.formpractice.forms.ProductForm;
import be.juvo.paul.formpractice.forms.ToDoForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
@Route("form_practice")
//@PWA(name = "Vaadin Application",
//        shortName = "Vaadin App",
//        description = "This is an example Vaadin application.",
//        enableInstallPrompt = false)
//@CssImport("./styles/shared-styles.css")
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    @Autowired
    private GreeterForm greeterForm;
    @Autowired
    private BinderUserForm binderUserForm;

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     */
    public MainView(
            GreeterForm greeterForm,
            BinderUserForm binderUserForm
    ) {
        this.greeterForm = greeterForm;
        this.binderUserForm = binderUserForm;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(binderUserForm, new ProductForm(), new ToDoForm());

        add(greeterForm, horizontalLayout);
    }

}
