package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.domain.managers.*
import dk.eboks.app.injection.modules.*
import dk.eboks.app.network.Api
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

    // For Interactor Injection
    fun plus(presentationModule: PresentationModule): PresentationComponent

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
}