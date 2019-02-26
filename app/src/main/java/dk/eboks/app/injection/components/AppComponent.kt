package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.injection.modules.AppModule
import dk.eboks.app.injection.modules.ExecutorModule
import dk.eboks.app.injection.modules.InteractorModule
import dk.eboks.app.injection.modules.RepositoryModule
import dk.eboks.app.injection.modules.StorageModule
import dk.eboks.app.injection.modules.UtilModule
import dk.eboks.app.network.Api
import dk.eboks.app.network.RestModule
import dk.eboks.app.network.managers.protocol.EAuth2
import dk.eboks.app.storage.base.ICacheStore
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

@Component(
    modules = [
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RepositoryModule::class,
        StorageModule::class,
        UtilModule::class
    ]
)

@AppScope
interface AppComponent {
    fun inject(app: App)
    fun inject(t: PermissionRequestActivity)
    fun inject(eAuth2: EAuth2)
    fun inject(t: ICacheStore)

    // For Interactor Injection
    fun plus(): PresentationComponent

    // expose functions to components dependent on this component
    fun executor(): Executor

    fun api(): Api

    // managers
    fun prefManager(): PrefManager

    fun appStateManager(): AppStateManager
    fun uiManager(): UIManager
    fun fileCacheManager(): FileCacheManager
    fun downloadManager(): DownloadManager
    fun eboksFormatter(): EboksFormatter
    fun permissionManager(): PermissionManager
    fun userManager(): UserManager
    fun userSettingsManager(): UserSettingsManager
}