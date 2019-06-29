package tk.leoforney.ourrequest.controller.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import tk.leoforney.ourrequest.model.spotify.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Tag("sortable-jquery")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
@JavaScript("frontend://bower_components/Sortable/Sortable.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/jquery-sortablejs@latest/jquery-sortable.js")
@JavaScript("frontend://js/list.js")
@StyleSheet("frontend://styles/list.css")
public class SortableList extends Component implements HasComponents {

    public enum SortOrder {
        REGULAR,
        SHUFFLE,
        REVERSE
    }

    private List<Track> requestedTracks, regularTracks, allTracks, tempList;
    private static ObjectMapper mapper;
    private SortOrder order;

    public SortableList(PlayerComponent component, SortOrder order) {
        this.component = component;
        this.order = order;
        setId("sortableList");
    }

    public void addItem(Component component) {
        add(component);
    }

    public void addTrackList(List<Track> requestedTracks, List<Track> regularTracks) {
        getElement().removeAllChildren();
        this.regularTracks = regularTracks;
        this.requestedTracks = requestedTracks;
        allTracks = new ArrayList<>();
        for (Track track : requestedTracks) {
            allTracks.add(track);
            add(new ListItem(track, true));
        }
        if (order.equals(SortOrder.REGULAR)) {
        } else if (order.equals(SortOrder.REVERSE)) {
            Collections.reverse(regularTracks);
        } else if (order.equals(SortOrder.SHUFFLE)) {
            Collections.shuffle(regularTracks);
        }
        for (Track track : regularTracks) {
            allTracks.add(track);
            add(new ListItem(track, false));
        }

    }

    public List<Track> getCurrentTrackListOrder() {
        if (tempList == null) tempList = new ArrayList<>();
        return tempList;
    }

    @ClientCallable
    public void nextTrackJs() {
        if (component != null) {
            component.playSong(tempList.get(index));
        }
    }

    @ClientCallable
    public void updateListOrder(String jsonData) {
        if (mapper == null) mapper = new ObjectMapper();
        List<String> trackIds = null;
        try {
            trackIds = mapper.readValue(jsonData, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String id : trackIds) {
            for (Track track : allTracks) {
                if (track.getId().equals(id)) {
                    tempList.add(track);
                    break;
                } else {
                    add(new ListItem(track, true));
                }
            }
        }
    }

    public void addRequestedTrack(Track track) {
        if (track != null) {
            System.out.println(track.getName());
            requestedTracks.add(track);
            add(new ListItem(track, true));
        }
    }

    private PlayerComponent component;
    private int index = 0;

    public void nextSong(int index) {
        tempList = new ArrayList<>();
        this.index = index;
        getElement().executeJavaScript("nextSong()");
    }


}
