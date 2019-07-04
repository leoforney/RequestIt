$(document).ready(function(){
    $("#sortableList").sortable({
        selectedClass: 'selected', // The class applied to the selected items
        animation: 150
    });
});

function retrieveOrder() {
    const songIds = [];
    $("#sortableList").children().map(function(){
        songIds.push(this.firstChild.getAttribute("id"));
    });
    const jsonString = JSON.stringify(songIds);
    document.getElementById("sortableList").$server.updateListOrder(jsonString);
    return jsonString;
}

var lastSongPlayedUri;
var lastSongPlayedIndex;


function nextSong() {
    /*
    jQuery(document).ready(function ($) {
        $("#sortableList").children().map(function(){
            if (this.firstChild.getAttribute("uri") == lastSongPlayedUri) {
                var child = this;
                var parent = child.parentNode;
                var index = Array.prototype.indexOf.call(parent.children, child);
                var added = index+1;
                console.log(lastSongPlayedIndex + " : " + (added));
            }
        });
    });*/

    // TODO: Finish this shit

    lastSongPlayedUri = $("#sortableList").children().eq(lastSongPlayedIndex + 1).children().eq(0).attr("uri");
    $("#sortableList").children().eq(lastSongPlayedIndex + 1).children().children().eq(0).addClass("played");
    lastSongPlayedIndex = lastSongPlayedIndex + 1;
    play(lastSongPlayedUri + "\"");
}

function playFirstSong() {
    if (lastSongPlayedUri === undefined) {
        jQuery(document).ready(function ($) {
            lastSongPlayedUri = $("#sortableList").children().eq(0).children().eq(0).attr("uri");
        });
        $("#sortableList").children().eq(0).children().children().eq(0).addClass("played");
        lastSongPlayedIndex = 0;
        play($("#sortableList").children().eq(0).children().eq(0).attr("uri") + "\"");
    }
}