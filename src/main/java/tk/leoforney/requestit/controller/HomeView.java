package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import java.util.function.Consumer;

@Route("")
@PageTitle("RequestIt | Home")
@Theme(value = Material.class, variant = Material.DARK)
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(new H2("Hello!"));
        Button dashboard = new Button("Dashboard");
        dashboard.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> dashboard.getUI().ifPresent(ui -> {
            ui.navigate("dashboard");
            ui.getPage().reload();
        }));
        add(dashboard);
    }

}