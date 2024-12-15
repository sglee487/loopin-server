package sg.snserver.hex.domain.entities

data class Plays(
    var currentPlays:MutableMap<String, NewPlayItem>, // playlistId, current play list item
    var playListsQueues:MutableMap<String, PlayListQueues>, // playlistId, prev & next list items
): Base()
