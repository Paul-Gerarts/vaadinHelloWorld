package be.juvo.paul.formpractice.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Label;
import lombok.Getter;
import lombok.Setter;

@Tag("custom-context-menu")
@Getter
@Setter
public class CustomContextMenu {

    private ContextMenu contextMenu = new ContextMenu();

    public CustomContextMenu() {

        // right click on the current screen should enable the contextMenu
        Component target = createTargetComponent();
        contextMenu.setTarget(target);
        Label message = new Label("-");
        contextMenu.addItem("First menu item", e -> message.setText("Clicked on the first item"));
        contextMenu.addItem("Second menu item", e -> message.setText("Clicked on the second item"));
        // The created MenuItem component can be saved for later use
        MenuItem item = contextMenu.addItem("Disabled menu item", e -> message.setText("This cannot happen"));
        item.setEnabled(false);
    }

    private Component createTargetComponent() {
        // TODO find out how to create a targetComponent
        return null;
    }
}
