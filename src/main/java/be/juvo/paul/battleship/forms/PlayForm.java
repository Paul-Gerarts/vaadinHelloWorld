package be.juvo.paul.battleship.forms;

import be.juvo.paul.battleship.factories.GridBuilder;
import be.juvo.paul.formpractice.GreetService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class PlayForm extends VerticalLayout {

    private GreetService greetService;
    private GridBuilder gridBuilder;
    private HorizontalLayout gamingGrid;

    @Autowired
    public PlayForm(
            GreetService greetService,
            GridBuilder gridBuilder
    ) {
        this.greetService = greetService;
        this.gridBuilder = gridBuilder;

        // game configuration
        TextField textField = new TextField("Your name");

        ComboBox<String> category = new ComboBox<>("Grid size");
        category.setItems("10");

        Button startGame = new Button("Start game",
                e -> {
                    Notification.show(greetService.greet(textField.getValue(), getGridSize(category)));
                    // grid
                    this.gamingGrid = new HorizontalLayout();
                    this.gamingGrid.setSpacing(false);
                    gridBuilder.buildGrid(textField.getValue(), getGridSize(category)).forEach(grid -> this.gamingGrid.add(grid));
                    add(this.gamingGrid);
                });
        startGame.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        startGame.addClickShortcut(Key.ENTER);

        HorizontalLayout initiators = new HorizontalLayout(textField, category);

        add(initiators);
        add(startGame);
    }

    private int getGridSize(ComboBox<String> category) {
        return null == category.getValue() ? 0 : Integer.parseInt(category.getValue());
    }

}
