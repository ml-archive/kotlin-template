package dk.nodes.template.domain.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

abstract class RxLiveDataInteractor<Input : Any, Output> {
    private var disposable: Disposable? = null
    private val subject = MutableLiveData<Output>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private lateinit var input: Input
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun setInput(input: Input) {
        this.input = input
        setSource(createObservable(input))
    }

    suspend fun run() {
        _loading.postValue(true)
        execute(input)
        _loading.postValue(false)
    }

    protected abstract suspend fun execute(input: Input)

    protected abstract fun createObservable(input: Input): Flowable<Output>

    fun clear() {
        disposable?.dispose()
        disposable = null
    }

    fun observe(): LiveData<Output> = subject

    private fun setSource(source: Flowable<Output>) {
        disposable?.dispose()
        disposable = source.subscribe(subject::postValue, errorLiveData::postValue)
    }
}