package dk.nodes.arch.domain.interactor

import dk.nodes.arch.extensions.toFlowable
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class SubjectInteractor<P : Any, EP, T> :
    Interactor<EP> {
    private var disposable: Disposable? = null
    private val subject: BehaviorSubject<T> = BehaviorSubject.create()

    private val _loading = BehaviorSubject.createDefault(false)
    val loading = _loading.toFlowable()

    private lateinit var params: P

    fun setParams(params: P) {
        this.params = params
        setSource(createObservable(params))
    }

    final override suspend fun invoke(executeParams: EP) {
        _loading.onNext(true)
        execute(params, executeParams)
        _loading.onNext(false)
    }

    protected abstract suspend fun execute(params: P, executeParams: EP)

    protected abstract fun createObservable(params: P): Flowable<T>

    fun clear() {
        disposable?.dispose()
        disposable = null
    }

    fun observe(): Flowable<T> = subject.toFlowable()

    private fun setSource(source: Flowable<T>) {
        disposable?.dispose()
        disposable = source.subscribe(subject::onNext, subject::onError)
    }
}
