package com.loopin.streaming_service.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("stream_session")
data class StreamSession(
    @Id
    val id: Long? = null,
    
    @Column("stream_key")
    val streamKey: String,
    
    @Column("title")
    val title: String,
    
    @Column("description")
    val description: String? = null,
    
    @Column("status")
    val status: StreamStatus,
    
    @Column("streamer_id")
    val streamerId: String,
    
    @Column("resolution")
    val resolution: String = "720p",
    
    @Column("bitrate")
    val bitrate: Int = 2500,
    
    @Column("viewers_count")
    val viewersCount: Int = 0,
    
    @CreatedDate
    @Column("created_at")
    val createdAt: Instant? = null,
    
    @LastModifiedDate
    @Column("updated_at")
    val updatedAt: Instant? = null
) {
    companion object {
        fun create(
            title: String,
            description: String?,
            streamerId: String,
            resolution: String = "720p",
            bitrate: Int = 2500
        ): StreamSession {
            return StreamSession(
                streamKey = UUID.randomUUID().toString(),
                title = title,
                description = description,
                status = StreamStatus.CREATED,
                streamerId = streamerId,
                resolution = resolution,
                bitrate = bitrate
            )
        }
    }
}