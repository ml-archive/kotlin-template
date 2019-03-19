package dk.eboks.app.presentation.ui.channels.components.requirements

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.Requirement
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChannelRequirementsComponentPresenterTest {

    private lateinit var presenter: ChannelRequirementsComponentPresenter
    private val viewMock = mockk<ChannelRequirementsComponentContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ChannelRequirementsComponentPresenter()
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun setup() {
        val channel = mockk<Channel>(relaxed = true)
        val requirementVerified = Requirement(
            "",
            verified = false,
            value = null,
            type = null
        )
        val requirementNotVerified = Requirement(
            "",
            verified = true,
            value = null,
            type = null
        )
        every { channel.requirements } returns listOf(
            requirementNotVerified,
            requirementVerified,
            requirementNotVerified,
            requirementVerified
        )
        presenter.setup(channel)
        verify {
            viewMock.setupView(channel)
            viewMock.showUnverifiedRequirements(listOf(requirementVerified, requirementVerified))
        }
    }
}