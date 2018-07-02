package dk.eboks.app.presentation.base

/**
 * Created by bison on 18-02-2018.
 */
interface ComponentBaseView : BaseView {
    fun showProgress(show : Boolean)
    fun showEmpty(show : Boolean)
}