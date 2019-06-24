package tk.leoforney.ourrequest.controller.player;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import tk.leoforney.ourrequest.model.spotify.Track;

import java.util.List;

@Tag("sortable-jquery")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
@JavaScript("frontend://bower_components/Sortable/Sortable.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/jquery-sortablejs@latest/jquery-sortable.js")
@JavaScript("frontend://js/list.js")
@StyleSheet("frontend://styles/list.css")
public class SortableList extends Component implements HasComponents {

    List<Track> requestedTracks;
    List<Track> regularTracks;

    public SortableList() {
        setId("sortableList");
    }

    public void addItem(Component component) {
        add(component);
    }

    public void addTrackList(List<Track> requestedTracks, List<Track> regularTracks) {
        getElement().removeAllChildren();
        this.regularTracks = regularTracks;
        this.requestedTracks = requestedTracks;
        for (Track track: requestedTracks) {
            add(new ListItem(track, true));
        }
        for (Track track: regularTracks) {
            add(new ListItem(track, false));
        }
    }

    public List<Track> getCurrentTrackListOrder() {
        // Because of the lack of function to retrieve the data from the list, as objects
        // TODO: Deconstruct DOM and resolve into the list
        return null;
    }

}
