package dk.nodes.template.mvp

import java.lang.ref.WeakReference

/**
 * Created by joso on 06/09/16.
 * Implementation from: https://github.com/sockeqwe/mosby/blob/master/mvp-common/src/main/java/com/hannesdorfmann/mosby/mvp/MvpBasePresenter.java
 */
open class MvpBasePresenter<V : MvpView> : MvpPresenter<V> {

    private var weakReference: WeakReference<V>? = null

    override fun attachView(view: V) {
        weakReference = WeakReference(view)
    }

    override fun detachView() {
        weakReference?.clear()
        weakReference = null
        /*
        if (weakReference != null) {
            weakReference!!.clear()
            weakReference = null
        }
        */
    }

    fun getView2() : V? {
        return weakReference?.get()
    }

    // custom getter/setter wrapping the weakreference, equivalent to function imp above
    val view: V?
        get() = weakReference?.get()

    val isViewAttached: Boolean
        get() = weakReference?.get() != null
}
