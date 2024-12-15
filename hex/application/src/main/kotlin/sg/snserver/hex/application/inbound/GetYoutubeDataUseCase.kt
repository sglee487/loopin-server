package sg.snserver.hex.application.inbound

interface GetYoutubeDataUseCase {
    fun getPlaylist(playlistId: String, refresh: Boolean)
}