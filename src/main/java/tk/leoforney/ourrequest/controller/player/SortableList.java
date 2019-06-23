package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import tk.leoforney.ourrequest.model.spotify.Track;

import java.util.List;

@Tag("sortable-list")
@HtmlImport("frontend://bower_components/sortable-list/sortable-list.html")
@HtmlImport("frontend://html/list.html")
public class SortableList extends Component implements HasComponents {

    public SortableList() {

    }

    public void addItem(Component component) {
        add(component);
    }

    public void addTrackList(List<Track> tracks) {
        for (Track track: tracks) {
            add(new ListItem(track));
        }
    }

}
