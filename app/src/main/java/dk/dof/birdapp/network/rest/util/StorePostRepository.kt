package dk.dof.birdapp.network.rest

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nytimes.android.external.fs3.FileSystemPersister
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import dk.dof.birdapp.domain.models.Post
import dk.dof.birdapp.domain.repositories.PostRepository
import dk.dof.birdapp.domain.repositories.RepositoryException
import okio.BufferedSource

/**
 * Created by bison on 24-06-2017.
 */
class StorePostRepository(val api: Api, val gson: Gson, val context: Context) : PostRepository {

    val postStore: Store<List<Post>, Int> by lazy {
        StoreBuilder.parsedWithKey<Int, BufferedSource, List<Post>>()
                .fetcher { key -> api.getPostsBuffered() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.filesDir), { key -> "Post$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Post>>(gson, object : TypeToken<List<Post>>() {}.type))
                .open()
    }

    override fun getPosts(cached : Boolean): List<Post>
    {
        try {
            return if(cached) postStore.get(0).blockingGet() else postStore.fetch(0).blockingGet()
        }
        catch (e : Exception)
        {
            throw(RepositoryException(-1, e.message ?: "Unknown"))
        }
    }


}