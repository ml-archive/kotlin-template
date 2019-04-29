package dk.nodes.template.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.BaseAsyncInteractor

class LiveDataInteractor<T>(private val interactor: BaseAsyncInteractor<out T>) {

    private val mutableLiveData = MutableLiveData<AsyncResult<T>>()
    val liveData: LiveData<AsyncResult<T>> = mutableLiveData

    init {
        mutableLiveData.postValue(Uninitialized)
    }

    suspend operator fun invoke() {
        mutableLiveData.postValue(Loading())
        try {
            val result = interactor.invoke()
            mutableLiveData.postValue(Success(result))
        } catch (t: Throwable) {
            mutableLiveData.postValue(Error(t))
        }
    }
}

fun <T> BaseAsyncInteractor<T>.asLiveData(): LiveDataInteractor<T> {
    return LiveDataInteractor(this)
}