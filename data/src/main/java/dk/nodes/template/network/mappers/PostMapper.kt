package dk.nodes.template.network.mappers

import dk.nodes.template.domain.entities.Post
import dk.nodes.template.network.dto.PostDto

object PostMapper : EntityMapper<PostDto, Post> {
    override fun mapEntity(t: PostDto): Post {
        return Post(
            userId = t.userId,
            id = t.id,
            title = t.title,
            body = t.body
        )
    }
}