package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.repositories.*
import dk.eboks.app.network.rest.CategoriesRestRepository
import dk.eboks.app.network.rest.FoldersRestRepository
import dk.eboks.app.network.rest.MessagesRestRepository
import dk.eboks.app.network.rest.SendersRestRepository
import dk.eboks.app.storage.AppStateRepositoryImpl
import dk.eboks.app.storage.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun provideAppStateRepository(context: Context, gson: Gson) : AppStateRepository
    {
        return AppStateRepositoryImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideMessagesRepository(messageStore: MessageStore) : MessagesRepository
    {
        return MessagesRestRepository(messageStore)
    }


    @Provides
    @AppScope
    fun provideSendersRepository(senderStore: SenderStore) : SendersRepository
    {
        return SendersRestRepository(senderStore)
    }

    @Provides
    @AppScope
    fun provideCategoriesRepository(categoryStore: CategoryStore) : CategoriesRepository
    {
        return CategoriesRestRepository(categoryStore)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }

    @Provides
    @AppScope
    fun provideFoldersRepository(folderStore: FolderStore) : FoldersRepository
    {
        return FoldersRestRepository(folderStore)
    }
}