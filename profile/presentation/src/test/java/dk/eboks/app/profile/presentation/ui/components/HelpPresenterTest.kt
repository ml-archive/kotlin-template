package dk.eboks.app.profile.presentation.ui.components

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.domain.models.shared.ResourceLink
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class HelpPresenterTest {

    private val appConfig: AppConfig = mockk(relaxUnitFun = true)
    private val presenter: HelpPresenter = HelpPresenter(appConfig)
    private val view: HelpContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    @Test
    fun `Test On View Created`() {
        val link = ResourceLink("s", Link("text", "nodesagency.com"))
        every { appConfig.getResourceLinkByType("support") } returns link
        presenter.onViewCreated(view, lifecycle)

        verify { view.loadUrl(link.link.url) }
    }
}