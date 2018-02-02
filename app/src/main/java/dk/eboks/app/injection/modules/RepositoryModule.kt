package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.rest.FoldersRestRepository
import dk.eboks.app.network.rest.MessagesRestRepository
import dk.eboks.app.network.rest.SendersRestRepository
import dk.eboks.app.storage.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RepositoryModule {
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
    fun provideFoldersRepository(folderStore: FolderStore) : FoldersRepository
    {
        return FoldersRestRepository(folderStore)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }
}