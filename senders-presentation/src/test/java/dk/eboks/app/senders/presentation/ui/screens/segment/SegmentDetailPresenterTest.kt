package dk.eboks.app.senders.presentation.ui.screens.segment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.senders.interactors.GetSegmentInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class SegmentDetailPresenterTest {

    private lateinit var presenter: SegmentDetailPresenter
    private val getSegmentInteractor = mockk<GetSegmentInteractor>(relaxUnitFun = true)
    private val registerInteractor = mockk<RegisterInteractor>(relaxUnitFun = true)
    private val unregisterInteractor = mockk<UnRegisterInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<SegmentDetailContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = SegmentDetailPresenter(
            getSegmentInteractor,
            registerInteractor,
            unregisterInteractor
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test on get segment`() {
        val segment = mockk<Segment>()
        presenter.onGetSegment(segment)
        verify {
            viewMock.showSegment(segment)
        }
    }

    @Test
    fun `Test load segment`() {
        val segmentId = Random.nextLong()
        presenter.loadSegment(segmentId)
        verify {
            getSegmentInteractor.input = GetSegmentInteractor.Input(segmentId)
            getSegmentInteractor.run()
        }
    }

    @Test
    fun `Test register segment`() {
        val segmentId = Random.nextLong()
        presenter.registerSegment(segmentId)
        verify {
            registerInteractor.inputSegment = RegisterInteractor.InputSegment(segmentId)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test unregisterSegment`() {
        val segmentId = Random.nextLong()
        presenter.unregisterSegment(segmentId)
        verify {
            unregisterInteractor.inputSegment = UnRegisterInteractor.InputSegment(segmentId)
            unregisterInteractor.run()
        }
    }

    @Test
    fun `Test on success`() {
        presenter.onSuccess()
        verify {
            viewMock.showSuccess()
        }
    }

    @Test
    fun `Test on error`() {
        val message = "TEST"
        val viewError = ViewError(message = message)
        presenter.onError(viewError)
        verify {
            viewMock.showError(message)
        }
    }
}