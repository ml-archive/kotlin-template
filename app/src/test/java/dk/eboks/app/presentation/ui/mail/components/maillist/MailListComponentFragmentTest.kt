package dk.eboks.app.presentation.ui.mail.components.maillist

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentContract
import dk.eboks.app.presentation.isRefreshing
import dk.eboks.app.util.BundleKeys
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MailListComponentFragmentTest {

    private lateinit var scenario: FragmentScenario<MailListComponentFragment>
    private val presenter = mockk<MailListComponentContract.Presenter>(relaxUnitFun = true)
    private val formatter = mockk<EboksFormatter>(relaxUnitFun = true)
    private val adapter = mockk<MailMessagesAdapter>(relaxUnitFun = true)

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(
            bundleOf(
                BundleKeys.Arguments to MailListComponentFragment.Arguments()
            )
        )
        scenario.onFragment {
            it.presenter = presenter
            it.formatter = formatter
            it.adapter = adapter
        }
    }

    @After
    fun tearDown() {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun `Test show refresh progress`() {
        scenario.onFragment {
            it.showRefreshProgress(true)
        }
        onView(withId(R.id.refreshSrl)).check(matches(isRefreshing()))
        scenario.onFragment {
            it.showRefreshProgress(false)
        }
        onView(withId(R.id.refreshSrl)).check(matches(not(isRefreshing())))
    }

    @Test
    fun `Test show progress`() {
        scenario.onFragment {
            it.showProgress(true)
        }
        onView(withId(R.id.progressFl)).check(matches(isDisplayed()))
        scenario.onFragment {
            it.showProgress(false)
        }
        onView(withId(R.id.progressFl)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `Test show empty`() {
        scenario.onFragment {
            it.showEmpty(true)
        }
        onView(withId(R.id.emptyFl)).check(matches(isDisplayed()))
        onView(withId(R.id.contentFl)).check(matches(not(isDisplayed())))

        scenario.onFragment {
            it.showEmpty(false)
        }
        onView(withId(R.id.emptyFl)).check(matches(not(isDisplayed())))
        onView(withId(R.id.contentFl)).check(matches(isDisplayed()))
    }

    @Test
    fun `Test show messages`() {
        val messages = listOf<Message>(mockk(), mockk())
        val checkedList = mutableListOf<Message>()
        scenario.onFragment {
            it.checkedList = checkedList
            it.showMessages(messages)
        }
        verify {
            adapter.setData(messages)
            checkedList.clear()
        }
    }

    @Test
    fun `Test append messages`() {
        val messages = listOf<Message>(mockk(), mockk())
        scenario.onFragment {
            it.appendMessages(messages)
        }
        verify { adapter.addMessages(messages) }
    }
}