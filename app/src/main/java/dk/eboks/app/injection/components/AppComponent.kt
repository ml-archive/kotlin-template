package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.*
import dk.eboks.app.injection.modules.*
import dk.eboks.app.network.Api
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

@Component(modules = [
    AppModule::class,
    ExecutorModule::class,
    InteractorModule::class,
    RestModule::class,
    StoreModule::class,
    RepositoryModule::class,
    PresentationModule::class,
    StorageModule::class,
    UtilModule::class])

@AppScope
interface AppComponent
{
    fun inject(app: App)
    fun inject(t : PermissionRequestActivity)

    // expose functions to components dependent on this component
    fun executor() : Executor
    fun api() : Api

    // managers
    fun prefManager() : PrefManager
    fun appStateManager() : AppStateManager
    fun uiManager() : UIManager
    fun fileCacheManager() : FileCacheManager
    fun downloadManager() : DownloadManager
    fun eboksFormatter() : EboksFormatter
    fun permissionManager() : PermissionManager
    fun userManager() : UserManager

    // interactors
    fun loginInteractor() : LoginInteractor
    fun boostrapInteractor() : BootstrapInteractor
    fun getSendersInteractor() : GetSendersInteractor
    fun getSenderDetailInteractor() : GetSenderDetailInteractor
    fun getGetSegmentInteractor() : GetSegmentInteractor
    fun getCategoriesInteractor() : GetCategoriesInteractor
    fun getGetCollectionsInteractor() : GetCollectionsInteractor
    fun getMessagesInteractor() : GetMessagesInteractor
    fun getFoldersInteractor() : GetFoldersInteractor
    fun openMessageInteractor() : OpenMessageInteractor
    fun openFolderInteractor() : OpenFolderInteractor
    fun openAttachmentInteractor() : OpenAttachmentInteractor
    fun saveAttachmentInteractor() : SaveAttachmentInteractor
    fun getChannelsInteractor() : GetChannelsInteractor
    fun createUserInteractor() : CreateUserInteractor
    fun saveUserInteractor() : SaveUserInteractor
    fun deleteUserInteractor() : DeleteUserInteractor
    fun getUsersInteractor() : GetUsersInteractor
    fun getSenderCategoriesInteractor() : GetSenderCategoriesInteractor
    fun getChannelInteractor() : GetChannelInteractor
    fun registerInteractor(): RegisterInteractor
    fun unregisterInteractor(): UnRegisterInteractor
}