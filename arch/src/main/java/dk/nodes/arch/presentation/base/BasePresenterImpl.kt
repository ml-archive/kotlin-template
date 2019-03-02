package dk.nodes.arch.presentation.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.LinkedBlockingQueue
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

abstract class BasePresenterImpl<V> : BasePresenter<V>, LifecycleObserver {

    private val cachedViewActions = LinkedBlockingQueue<Runnable>()
    protected var view: V? = null
    protected var lifecycle: Lifecycle? = null
    protected var job: Job = Job()

    var mainCoroutineContext: CoroutineContext = Dispatchers.Main + job
        private set
    var ioCoroutineContext: CoroutineContext = Dispatchers.IO + job
        private set
    var defaultCoroutineContext: CoroutineContext = Dispatchers.Default + job
        private set

    private val mainScope = CoroutineScope(mainCoroutineContext)

    private val ioScope = CoroutineScope(ioCoroutineContext)

    private val defaultScope = CoroutineScope(defaultCoroutineContext)

    override fun onCreate(view: V, lifecycle: Lifecycle) {
        this.lifecycle = lifecycle

        lifecycle.addObserver(this)
    }

    override fun onViewCreated(view: V, lifecycle: Lifecycle) {
        this.view = view
        this.lifecycle = lifecycle

        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        while (!cachedViewActions.isEmpty() && view != null) {
            view?.let { cachedViewActions.poll().run() }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onViewDetached() {
        view = null
        cachedViewActions.clear()
        lifecycle?.removeObserver(this)
        job.cancel()
    }

    fun runAction(runnable: Runnable) {
        if (view != null) {
            view?.let {
                runnable.run()
            }
        } else {
            cachedViewActions.add(runnable)
        }
    }

    fun runAction(action: (V) -> Unit) {
        if (view != null) {
            view?.let {
                action(it)
            }
        } else {
            cachedViewActions.add(Runnable {
                action(view!!)
            })
        }
    }

    fun view(block: V.() -> Unit) {
        view?.let(block) ?: cachedViewActions.add(Runnable { view?.block() })
    }

    fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return mainScope.launch(context = mainCoroutineContext, block = block)
    }

    fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return ioScope.launch(context = ioCoroutineContext, block = block)
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return defaultScope.launch(context = defaultCoroutineContext, block = block)
    }

    fun <T> asyncOnUI(block: suspend CoroutineScope.() -> T): Deferred<T>  {
        return mainScope.async(context = mainCoroutineContext, block = block)
    }

    fun <T> asyncOnIO(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return ioScope.async(context = ioCoroutineContext, block = block)
    }

    fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return defaultScope.async(context = defaultCoroutineContext, block = block)
    }

    override fun activateTestMode(context: CoroutineContext) {
        mainCoroutineContext = context + job
        ioCoroutineContext = context + job
        defaultCoroutineContext = context + job
    }

}

@InternalCoroutinesApi
fun <T> runBlockingTest(presenter: BasePresenter<*>, block: suspend CoroutineScope.() -> T): T {
    return runBlocking(TestContext()) {
        presenter.activateTestMode(this.coroutineContext)
        return@runBlocking block.invoke(this)
    }
}

/**
 * Temporary workaround which sets delays used in Coroutines to 0
 * Can be replaced with Kotlin built-in solution when https://github.com/softagram/kotlinx.coroutines/pull/3 will be merged
 * For more info see: https://github.com/Kotlin/kotlinx.coroutines/issues/541
 * https://stackoverflow.com/a/49078296/1502079
 * */
@InternalCoroutinesApi
class TestContext : CoroutineDispatcher(), Delay {

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        continuation.resume(Unit)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        block.run()  // dispatch on calling thread
    }
}