package dk.nodes.template.presentation.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.BaseAsyncInteractor
import dk.nodes.template.domain.interactors.CompleteResult
import dk.nodes.template.domain.interactors.Fail
import dk.nodes.template.domain.interactors.InteractorResult
import dk.nodes.template.domain.interactors.Loading
import dk.nodes.template.domain.interactors.Success
import dk.nodes.template.domain.interactors.Uninitialized

class LiveDataInteractor<T>(private val interactor: BaseAsyncInteractor<out T>) {

    private val mutableLiveData = MutableLiveData<InteractorResult<T>>()
    val liveData: LiveData<InteractorResult<T>> = mutableLiveData

    init {
        mutableLiveData.postValue(Uninitialized)
    }

    suspend operator fun invoke() {
        mutableLiveData.postValue(Loading())
        try {
            val result = interactor.invoke()
            mutableLiveData.postValue(Success(result))
        } catch (t: Throwable) {
            mutableLiveData.postValue(Fail(t))
        }
    }
}

class ResultInteractor<T>(private val interactor: BaseAsyncInteractor<out T>) :
    BaseAsyncInteractor<CompleteResult<T>> {
    override suspend fun invoke(): CompleteResult<T> {
        return try {
            Success(interactor())
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}

fun <T> BaseAsyncInteractor<T>.asResult(): ResultInteractor<T> {
    return ResultInteractor(this)
}

fun <T> BaseAsyncInteractor<T>.asLiveData(): LiveDataInteractor<T> {
    return LiveDataInteractor(this)
}