package dk.dof.birdapp.domain.repositories

import dk.dof.birdapp.domain.models.Post


/**
 * Created by bison on 24-06-2017.
 */
interface PostRepository {
    fun getPosts(cached : Boolean = false) : List<Post>
}