package dk.eboks.app.presentation.ui.channels.components.opening

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.util.toBundle
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ChannelOpeningComponentFragmentTest {

    private lateinit var scenario: FragmentScenario<ChannelOpeningComponentFragment>

    @Before
    fun setup() {
    }

    @Test
    fun `Test show open state`() {
        val channel = mockk<Channel>(relaxed = true)
        scenario = launchFragmentInContainer(channel.toBundle())
        scenario.onFragment {
            it.showOpenState(channel)
        }
    }

    @Test
    fun showDisabledState() {
    }

    @Test
    fun showInstallState() {
    }

    @Test
    fun showVerifyState() {
    }

    @Test
    fun showStoreboxUserAlreadyExists() {
    }

    @Test
    fun showProgress() {
    }

    @Test
    fun showRequirementsDrawer() {
    }

    @Test
    fun openChannelContent() {
    }

    @Test
    fun openStoreBoxContent() {
    }

    @Test
    fun openEkeyContent() {
    }
}