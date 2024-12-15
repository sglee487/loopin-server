package sg.snserver.hex.adapter_outbound.jpa.entities

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PlaysEntity(
    var currentPlays:MutableMap<String, NewPlayItemEntity>, // playlistId, current play list item
    var playListsQueues:MutableMap<String, PlayListQueuesEntity>, // playlistId, prev & next list items
)
