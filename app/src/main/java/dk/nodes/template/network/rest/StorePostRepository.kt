package dk.nodes.template.network.rest

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nytimes.android.external.fs3.FileSystemPersister
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import okio.BufferedSource

class StorePostRepository(val api: Api, val gson: Gson, val context: Context) : PostRepository {

    private val postStore: Store<List<Post>, Int> by lazy {
        StoreBuilder.parsedWithKey<Int, BufferedSource, List<Post>>()
            .fetcher { key -> api.getPostsBuffered() }
            .persister(
                FileSystemPersister.create(
                    FileSystemFactory.create(context.filesDir)
                ) { key -> "Post$key" }
            )
            .parser(
                GsonParserFactory.createSourceParser<List<Post>>(
                    gson,
                    object : TypeToken<List<Post>>() {}.type
                )
            )
            .open()
    }

    override suspend fun getPosts(cached: Boolean): List<Post> {
        try {
            return if (cached) postStore.get(0).blockingGet() else postStore.fetch(0).blockingGet()
        } catch (e: Exception) {
            throw(RepositoryException(-1, e.message ?: "Unknown"))
        }
    }
}