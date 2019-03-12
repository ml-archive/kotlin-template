package dk.eboks.app.senders.presentation.ui.components.categories

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractor
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class CategoriesComponentPresenterTest {

    private lateinit var presenter: CategoriesComponentPresenter
    private val viewMock = mockk<CategoriesComponentContract.View>(relaxUnitFun = true)
    private val getSenderCategoriesInteractor =
        mockk<GetSenderCategoriesInteractor>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = CategoriesComponentPresenter(getSenderCategoriesInteractor)
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test get categories`() {
        presenter.getCategories()
        verify {
            getSenderCategoriesInteractor.input = GetSenderCategoriesInteractor.Input(true)
            getSenderCategoriesInteractor.output = presenter
            getSenderCategoriesInteractor.run()
        }
    }

    @Test
    fun `Test on get categories`() {
        val list = listOf(SenderCategory(Random.nextLong()), SenderCategory(Random.nextLong()))
        presenter.onGetCategories(list)
        verify {
            viewMock.showCategories(list)
        }
    }

    @Test
    fun `Test on get categories error`() {
        val error = ViewError()
        presenter.onGetCategoriesError(error)
        verify {
            viewMock.showErrorDialog(error)
        }
    }
}