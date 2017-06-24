package dk.nodes.template

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractorImpl
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.network.RestPostRepository
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class GetPostInteractorInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun getPosts() {
        // Context of the app under test.
        //val appContext = InstrumentationRegistry.getTargetContext()
        val repo = RestPostRepository(App.apiProxy())
        val interactor = GetPostsInteractorImpl(object : GetPostsInteractor.Callback {
            override fun onPostsLoaded(posts: List<Post>) {
                assertThat("100 posts were fetched", posts.size, CoreMatchers.`is`(IsEqual.equalTo(100)))
            }

            override fun onError(msg: String) {

            }
        }, repo)
        runBlocking {
            interactor.execute()
        }
    }
}
