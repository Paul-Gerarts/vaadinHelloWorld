package be.juvo.paul.formpractice;

import be.juvo.paul.sfaupgrade.view.SfaUpgradeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("layout_view")
public class AppLayOutView extends AppLayout {

    @Autowired
    private SfaUpgradeView sfaView;

    public AppLayOutView(SfaUpgradeView sfaView) {
        this.sfaView = sfaView;

        setPrimarySection(AppLayout.Section.DRAWER);
        Image img = new Image("https://i.imgur.com/GPpnszs.png", "Vaadin Logo");
        img.setHeight("44px");
        addToNavbar(new DrawerToggle(), img);
        Tab sfaUpgrade = new Tab("SFA upgrade tryout");
        sfaUpgrade.add(sfaView);
        Tabs tabs = new Tabs(sfaUpgrade);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);

        /*FormLayout nameLayout = new FormLayout();
        TextField titleField = new TextField();
        titleField.setLabel("Title");
        titleField.setPlaceholder("Sir");
        TextField firstNameField = new TextField();
        firstNameField.setLabel("First name");
        firstNameField.setPlaceholder("John");
        TextField lastNameField = new TextField();
        lastNameField.setLabel("Last name");
        lastNameField.setPlaceholder("Doe");
        nameLayout.add(titleField, firstNameField,
                lastNameField);
        nameLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("21em", 2),
                new FormLayout.ResponsiveStep("22em", 3));
        addToDrawer(nameLayout);

        SplitLayout layout = new SplitLayout(
                new Label("First content component"),
                new Label("Second content component"));

        addToDrawer(layout);*/
    }
}
