package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

// sample Page with onbeforeunload script
@Route(value = "test")
public class TestView extends VerticalLayout {

    Label statusLabel;

    public TestView() {
        setId("SomeView");
        add(new H4("Testing"));
        statusLabel = new Label("Status: ");
        add(statusLabel);
    }
}