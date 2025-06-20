package com.loopin.media_catalog_service.domain.repository

import com.loopin.media_catalog_service.domain.model.MediaPlaylist
import org.springframework.data.domain.*
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MediaPlaylistRepositoryCustomImpl(
    private val template: R2dbcEntityTemplate
): MediaPlaylistRepositoryCustom {
    override fun findAllBy(
        size: Int,
        sortBy: String,
        direction: String,
        offset: Long,
    ): Mono<Slice<MediaPlaylist>> {

        val sortDirection = Sort.Direction.fromString(direction)
        val pageable: Pageable = PageRequest.of(
            (offset / size).toInt(), // pageNumber = offset / size
            size,
            sortDirection,
            sortBy
        )

        /* 2) R2DBC Query – LIMIT = size+1 로 다음 페이지 존재 여부 확인 */
        val query = Query.empty()
            .sort(pageable.sort)            // createdAt DESC 등
            .limit(size + 1)
            .offset(offset)

        return template.select(MediaPlaylist::class.java)
            .matching(query)
            .all()              // Flux<MediaPlaylist>
            .collectList()      // Mono<List<MediaPlaylist>>
            .map { rows ->
                val hasNext = rows.size > size
                val content = if (hasNext) rows.subList(0, size) else rows
                SliceImpl(content, pageable, hasNext)
            }
    }
}