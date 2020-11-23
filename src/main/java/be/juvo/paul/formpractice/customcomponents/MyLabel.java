package be.juvo.paul.formpractice.customcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;

@Tag("my-label")
public class MyLabel extends Component {

    public void setText(String text) {
        getElement().setText(text);
    }

    public String getText() {
        return getElement().getText();
    }
}
