package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;
import tk.leoforney.ourrequest.listener.RequestListener;
import tk.leoforney.ourrequest.listener.RequestNotifier;
import tk.leoforney.ourrequest.model.spotify.Track;
import tk.leoforney.ourrequest.vaadin.MainLayout;

import java.util.function.Consumer;

// sample Page with onbeforeunload script
@Route(value = "test", layout = MainLayout.class)
public class TestView extends VerticalLayout implements RequestListener {

    Label statusLabel;

    RequestNotifier notifier = RequestNotifier.getInstance();

    public TestView() {
        setId("SomeView");
        add(new H4("Testing"));
        statusLabel = new Label("Status: ");
        add(statusLabel);
        notifier.addListener(this);
    }

    @Override
    public void trackAdded(Track addedTrack) {
        this.getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.access(new Command() {
                    @Override
                    public void execute() {
                        statusLabel.setText("Added track: " + addedTrack.getName());
                        ui.push();
                    }
                });
            }
        });
    }
}