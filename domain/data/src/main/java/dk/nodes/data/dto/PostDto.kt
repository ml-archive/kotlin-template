package dk.nodes.data.dto

data class PostDto(
    var userId: Int,
    var id: Int,
    var title: String,
    var body: String
)

fun PostDto.mapPost(): dk.nodes.template.core.entities.Post {
    return dk.nodes.template.core.entities.Post(
            id = id,
            title = title,
            body = body
    )
}

fun List<PostDto>.mapPosts(): List<dk.nodes.template.core.entities.Post> {
    return map { it.mapPost() }
}