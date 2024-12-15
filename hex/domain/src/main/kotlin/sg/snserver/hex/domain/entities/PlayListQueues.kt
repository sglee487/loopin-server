package sg.snserver.hex.domain.entities

data class PlayListQueues(
    var prev: MutableList<NewPlayItem>,
    var next: MutableList<NewPlayItem>,
): Base()
