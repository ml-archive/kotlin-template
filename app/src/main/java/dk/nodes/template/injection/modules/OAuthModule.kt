package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import dk.nodes.okhttputils.oauth.OAuthAuthenticator
import dk.nodes.okhttputils.oauth.OAuthCallback
import dk.nodes.okhttputils.oauth.OAuthInterceptor
import dk.nodes.okhttputils.oauth.OAuthRepository
import dk.nodes.okhttputils.oauth.entities.OAuthHeader
import dk.nodes.data.network.oauth.OAuthCallbackImpl
import dk.nodes.data.network.oauth.OAuthPreferencesRepository
import javax.inject.Singleton

@Module(includes = [OAuthModule.BindingModule::class])
class OAuthModule {

    @Module
    interface BindingModule {

        @Binds
        @Singleton
        fun bindOAuthRepository(repository: OAuthPreferencesRepository): OAuthRepository

        @Binds
        @Singleton
        fun bindOAuthCallback(oAuthCallback: OAuthCallbackImpl): OAuthCallback
    }

    @Provides
    @Singleton
    fun provideOAuthHeader(): OAuthHeader {
        // modify header type & name here
        return OAuthHeader()
    }

    @Provides
    @Singleton
    fun provideOAuthInterceptor(repository: OAuthRepository, oAuthHeader: OAuthHeader): OAuthInterceptor {
        return OAuthInterceptor(repository, oAuthHeader)
    }

    @Provides
    @Singleton
    fun provideOAuthAuthenticator(
            repository: OAuthRepository,
            oAuthHeader: OAuthHeader,
            oAuthCallback: OAuthCallback
    ): OAuthAuthenticator {
        return OAuthAuthenticator(repository, oAuthCallback, oAuthHeader)
    }
}