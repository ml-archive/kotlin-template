package dk.nodes.template.domain.repositories

import dk.nodes.template.domain.models.Post

/**
 * Created by bison on 24-06-2017.
 */
interface PostRepository {
    fun getPosts(cached : Boolean = false) : List<Post>
}