package dk.eboks.app.profile.presentation.ui.components

import dk.eboks.app.domain.config.AppConfig
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class PrivacyPresenterTest {

    private val baseUrl = "http://nodesagency.com/"
    private val presenter = PrivacyPresenter(baseUrl)
    private val view: PrivacyContract.View = mockk(relaxUnitFun = true)

    @Test
    fun `Test On View Created`() {
        presenter.onViewCreated(view, mockk(relaxUnitFun = true))
        verify {
            view.loadUrl("${baseUrl}resources/privacypolicy")
        }
    }
}