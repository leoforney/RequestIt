package tk.leoforney.ourrequest.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import tk.leoforney.ourrequest.model.Rating;

public class RatingDialog extends VerticalLayout {

    StarRating starRating;
    Button submitButton;
    TextField textField;

    public RatingDialog() {

        add(new Label("Please rate how easy that was"));

        starRating = new StarRating();
        add(starRating);

        textField = new TextField("Feedback?");
        add(textField);

        submitButton = new Button("Submit");
        add(submitButton);

    }

    public Rating getRating() {
        Rating rating = new Rating();
        rating.setMessage(textField.getValue());
        rating.setRating(starRating.getRating());
        return rating;
    }

}
