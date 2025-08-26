package com.loopin.streaming_service.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("video_segment")
data class VideoSegment(
    @Id
    val id: Long? = null,
    
    @Column("stream_session_id")
    val streamSessionId: Long,
    
    @Column("segment_number")
    val segmentNumber: Int,
    
    @Column("file_path")
    val filePath: String,
    
    @Column("duration")
    val duration: Double,
    
    @Column("resolution")
    val resolution: String,
    
    @Column("file_size")
    val fileSize: Long,
    
    @CreatedDate
    @Column("created_at")
    val createdAt: Instant? = null
)