package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.mockk

class LoginInteractorImplTest {
    private val executor = TestExecutor()
    private val api = mockk<Api>()
    private val appStateManager = mockk<AppStateManager>()
    private val userManager = mockk<UserManager>()
    private val userSettingsManager = mockk<UserSettingsManager>()
    private val authClient = mockk<AuthClient>()
    private val cacheManager = mockk<CacheManager>()
    private val foldersRepositoryMail = mockk<MailCategoriesRepository>()
    private val interactor = LoginInteractorImpl(
        executor,
        api,
        appStateManager,
        userManager,
        userSettingsManager,
        authClient,
        cacheManager,
        foldersRepositoryMail
    )



}