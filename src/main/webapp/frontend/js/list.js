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

function nextSong() {
    retrieveOrder();
    document.getElementById("sortableList").$server.nextTrackJs();

}