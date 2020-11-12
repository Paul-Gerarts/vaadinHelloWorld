package be.juvo.paul.battleship;

import be.juvo.paul.battleship.forms.PlayForm;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is a batteship application intended for practicing Vaadin 14.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class BattleshipView extends VerticalLayout {

    @Autowired
    private PlayForm playForm;

    public BattleshipView(PlayForm playForm) {
        this.playForm = playForm;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new PlayForm());

        add(playForm, horizontalLayout);
    }
}
