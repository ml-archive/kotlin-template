package dk.nodes.template.domain.entities

data class Post(
    var userId: Int,
    var id: Int,
    var title: String,
    var body: String
)