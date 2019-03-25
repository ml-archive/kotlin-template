package dk.eboks.app.presentation.ui.channels.components.opening

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.util.toBundle
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChannelOpeningComponentFragmentTest {

    private lateinit var scenario: FragmentScenario<ChannelOpeningComponentFragment>

    @Before
    fun setup() {
    }

    @Test
    fun `Test show open state`() {
        val channel = mockk<Channel>(relaxed = true)
        scenario = launchFragmentInContainer(
            fragmentArgs = channel.toBundle(),
            themeResId = R.style.AppTheme
        )
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