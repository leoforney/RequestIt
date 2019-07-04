package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;

@HtmlImport("frontend://bower_components/star-rating/star-rating.html")
@Tag("star-rating")
public class StarRating extends Div {

    public StarRating() {
        getElement().setProperty("rating", "4");
    }

    public int getRating() {
        return Integer.valueOf(getElement().getProperty("rating"));
    }


}
