package dk.nodes.template.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.CompleteResult
import dk.nodes.template.domain.interactors.Fail
import dk.nodes.template.domain.interactors.Interactor
import dk.nodes.template.domain.interactors.InteractorResult
import dk.nodes.template.domain.interactors.Loading
import dk.nodes.template.domain.interactors.Success
import dk.nodes.template.domain.interactors.Uninitialized
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class LiveDataInteractor2<I, T> {
    private val mediatorLiveData = MediatorLiveData<T>()
    abstract fun createLiveData(input: I): LiveData<T>
    private var previousSource: LiveData<T>? = null
    fun invoke(input: I) {
        previousSource?.let(mediatorLiveData::removeSource)
        val source = createLiveData(input)
        mediatorLiveData.addSource(source, mediatorLiveData::setValue)
        previousSource = source
    }

    val liveData: LiveData<T> = mediatorLiveData
}

abstract class FlowableInteractor<I : Any, T> {
    private val subject: Subject<I> = BehaviorSubject.create()
    val flowable = subject.switchMap {
        createObservable(it).toObservable()
    }.toFlowable(BackpressureStrategy.LATEST)!!

    operator fun invoke(input: I) = subject.onNext(input)

    protected abstract fun createObservable(input: I): Flowable<T>
}

private class RxInteractorImpl<I, O>(private val interactor: Interactor<I, out O>) :
    RxInteractor<I, O> {
    override suspend fun invoke(input: I) {
        subject.onNext(Loading())
        try {
            subject.onNext(Success(interactor(input)))
        } catch (t: Throwable) {
            subject.onError(t)
        }
    }

    override fun observe() = subject.toFlowable(BackpressureStrategy.LATEST)!!
    private val subject = BehaviorSubject.createDefault<InteractorResult<O>>(Uninitialized)
}

fun <I, O> Interactor<I, O>.asRx(): RxInteractor<I, O> {
    return RxInteractorImpl(this)
}

interface RxInteractor<I, O> : Interactor<I, Unit> {
    fun observe(): Flowable<InteractorResult<O>>
}

interface LiveDataInteractor<I, O> : Interactor<I, Unit> {
    val liveData: LiveData<InteractorResult<O>>
}

interface ResultInteractor<I, O> : Interactor<I, CompleteResult<O>>

@ExperimentalCoroutinesApi
interface ChannelInteractor<I, O> : Interactor<I, Unit> {
    fun receive(): ReceiveChannel<InteractorResult<O>>
}

interface FlowInteractor<I, O> : Interactor<I, Flow<InteractorResult<O>>>

private class LiveDataInteractorImpl<I, O>(private val interactor: Interactor<I, out O>) :
    LiveDataInteractor<I, O> {

    private val mutableLiveData = MutableLiveData<InteractorResult<O>>(Uninitialized)

    override val liveData = mutableLiveData
    override suspend operator fun invoke(input: I) {
        mutableLiveData.postValue(Loading())
        try {
            val result = interactor(input)
            mutableLiveData.postValue(Success(result))
        } catch (t: Throwable) {
            mutableLiveData.postValue(Fail(t))
        }
    }
}

@ExperimentalCoroutinesApi
private class ChannelInteractorImpl<I, O>(private val interactor: Interactor<I, out O>) :
    ChannelInteractor<I, O> {

    private val channel = BroadcastChannel<InteractorResult<O>>(Channel.CONFLATED).apply {
        offer(Uninitialized)
    }

    override fun receive() = channel.openSubscription()

    override suspend operator fun invoke(input: I) {
        channel.offer(Loading())
        try {
            channel.offer(Success(interactor(input)))
        } catch (t: Throwable) {
            channel.offer(Fail(t))
        }
    }
}

private class ResultInteractorImpl<I, O>(private val interactor: Interactor<I, out O>) :
    ResultInteractor<I, O> {
    override suspend fun invoke(input: I): CompleteResult<O> {
        return try {
            Success(interactor(input))
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}

private class FlowInteractorImpl<I, O>(private val interactor: Interactor<I, out O>) :
    FlowInteractor<I, O> {
    override suspend fun invoke(input: I): Flow<InteractorResult<O>> {
        return flow {
            emit(Loading<O>())
            try {
                emit(Success(interactor(input)))
            } catch (t: Throwable) {
                emit(Fail(t))
            }
        }
    }
}

fun <I, O> Interactor<I, O>.asResult(): ResultInteractor<I, O> {
    return ResultInteractorImpl(this)
}

fun <I, O> Interactor<I, O>.asLiveData(): LiveDataInteractor<I, O> {
    return LiveDataInteractorImpl(this)
}

@ExperimentalCoroutinesApi
fun <I, O> Interactor<I, O>.asChannel(): ChannelInteractor<I, O> {
    return ChannelInteractorImpl(this)
}

fun <I, O> Interactor<I, O>.asFlow(): FlowInteractor<I, O> {
    return FlowInteractorImpl(this)
}

fun <I> CoroutineScope.launchInteractor(
    interactor: Interactor<I, Unit>,
    input: I,
    coroutineContext: CoroutineContext = Dispatchers.IO
) {
    launch(coroutineContext) { interactor(input) }
}

fun CoroutineScope.launchInteractor(
    interactor: Interactor<Unit, Unit>,
    coroutineContext: CoroutineContext = Dispatchers.IO
) {
    launchInteractor(interactor, Unit, coroutineContext)
}

suspend fun <I, T> runInteractor(
    interactor: Interactor<I, T>,
    input: I,
    coroutineContext: CoroutineContext = Dispatchers.IO
): T {
    return withContext(coroutineContext) { interactor(input) }
}

suspend fun <O> runInteractor(
    interactor: Interactor<Unit, O>,
    coroutineContext: CoroutineContext = Dispatchers.IO
): O {
    return withContext(coroutineContext) { interactor(Unit) }
}

fun <T, R> InteractorResult<T>.isSuccess(block: (T) -> R): InteractorResult<T> {
    if (this is Success) {
        block(this.data)
    }
    return this
}

fun <T, R> InteractorResult<T>.isError(block: (throwable: Throwable) -> R): InteractorResult<T> {
    if (this is Fail) {
        block(this.throwable)
    }
    return this
}