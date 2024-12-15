package sg.snserver.hex.adapter_outbound.jpa.entities

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PlayListQueuesEntity(
    var prev: MutableList<NewPlayItemEntity>,
    var next: MutableList<NewPlayItemEntity>,
)
