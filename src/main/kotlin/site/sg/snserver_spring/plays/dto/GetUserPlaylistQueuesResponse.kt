package site.sg.snserver_spring.plays.dto

class GetUserPlaylistQueuesResponse(
    val prev: List<PlayListItemResponse>,
    val next: List<PlayListItemResponse>
) {
}