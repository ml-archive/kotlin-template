package dk.eboks.app.senders.presentation.ui.components

import dk.eboks.app.domain.models.sender.Sender
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.random.Random

class SenderGroupsComponentPresenterTest {

    @Test
    fun `Test get sender groups`() {
        val presenter = SenderGroupsComponentPresenter()
        val viewMock = mockk<SenderGroupsComponentContract.View>(relaxUnitFun = true)
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
        val sender = Sender(id = Random.nextLong(), groups = listOf(mockk()))
        presenter.getSenderGroups(sender)
        verify { viewMock.showSenderGroups(sender) }
        presenter.onViewDetached()
    }
}