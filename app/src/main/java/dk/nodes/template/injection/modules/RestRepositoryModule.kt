package dk.nodes.template.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.network.rest.Api
import dk.nodes.template.network.rest.StorePostRepository

@Module
class RestRepositoryModule {
    @Provides
    @AppScope
    fun providePostRepository(api: Api, gson: Gson, context: Context): PostRepository {
        return StorePostRepository(api, gson, context)
    }
}