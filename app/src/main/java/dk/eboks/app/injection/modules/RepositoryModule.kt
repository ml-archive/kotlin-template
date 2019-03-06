package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.repositories.ChannelsRestRepository
import dk.eboks.app.network.repositories.CollectionsRestRepository
import dk.eboks.app.network.repositories.FoldersRestRepository
import dk.eboks.app.network.repositories.MailCategoriesRestRepository
import dk.eboks.app.network.repositories.MessagesRestRepository
import dk.eboks.app.network.repositories.SenderCategoriesRestRepository
import dk.eboks.app.network.repositories.SendersRestRepository
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.eboks.app.network.repositories.UserRestRepository
import dk.eboks.app.storage.repositories.AppStateRepositoryImpl
import dk.eboks.app.storage.repositories.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
abstract class RepositoryModule {
    @Binds
    @AppScope
    abstract fun bindAppStateRepository(repository: AppStateRepositoryImpl): AppStateRepository

    @Binds
    @AppScope
    abstract fun bindSignupRestRepository(repository: SignupRestRepository): SignupRepository

    @Binds
    @AppScope
    abstract fun bindUserRestRepository(repository: UserRestRepository): UserRepository

    @Binds
    @AppScope
    abstract fun bindMessagesRepository(repository: MessagesRestRepository): MessagesRepository

    @Binds
    @AppScope
    abstract fun bindSendersRepository(repository: SendersRestRepository): SendersRepository

    @Binds
    @AppScope
    abstract fun bindSenderCategoriesRepository(repository: SenderCategoriesRestRepository): SenderCategoriesRepository

    @Binds
    @AppScope
    abstract fun bindMailCategoriesRepository(repository: MailCategoriesRestRepository): MailCategoriesRepository

    @Binds
    @AppScope
    abstract fun bindSettingsRepository(repository: SharedPrefsSettingsRepository): SettingsRepository

    @Binds
    @AppScope
    abstract fun bindFoldersRepository(repository: FoldersRestRepository): FoldersRepository

    @Binds
    @AppScope
    abstract fun bindChannelsRepository(repository: ChannelsRestRepository): ChannelsRepository

    @Binds
    @AppScope
    abstract fun bindCollectionsRepository(repository: CollectionsRestRepository): CollectionsRepository
}