package dk.dof.birdapp.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.dof.birdapp.domain.repositories.PostRepository
import dk.dof.birdapp.network.rest.Api
import dk.dof.birdapp.network.rest.StorePostRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RestRepositoryModule {
    @Provides
    @AppScope
    fun providePostRepository(api: Api, gson: Gson, context: Context) : PostRepository
    {
        return StorePostRepository(api, gson, context)
    }
}