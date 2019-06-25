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
    document.getElementById("sortableList").$server.updateListOrder(JSON.stringify(songIds));
}