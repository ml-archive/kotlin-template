package dk.eboks.app.domain.repositories

/**
 * Created by bison on 24-06-2017.
 */
interface PostRepository {
    fun getPosts() : List<dk.eboks.app.domain.models.Post>
}