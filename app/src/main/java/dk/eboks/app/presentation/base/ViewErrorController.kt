package dk.eboks.app.presentation.base

import android.content.Context
import android.support.v7.app.AlertDialog
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.guard
import timber.log.Timber

/**
 * Created by bison on 22/03/2018.
 */
typealias CloseFunction = (()->Unit)?

class ViewErrorController(val context: Context, val closeFunction: CloseFunction = null) {

    fun showErrorDialog(error : ViewError)
    {
        if(!error.shouldDisplay)
        {
            Timber.e("Error not displayed to user: $error")
            closeFunction?.let { if(error.shouldCloseView) it() }.guard {
                Timber.e("View closure was requested but view haven't specified a CloseFunction in ViewErrorController constructor, ignoring.")
            }
            return
        }
        val builder = AlertDialog.Builder(context)
        error.title?.let { builder.setTitle(it) }
        error.message?.let { msg ->
            builder.setMessage(msg)
        }.guard {
            Timber.e("Not possible to show error since description is null, showing generic")
            if(error.title == null)
                builder.setTitle(Translation.error.genericTitle)
            builder.setMessage(Translation.error.genericMessage)
        }
        val label = if(error.shouldCloseView) Translation.defaultSection.close else Translation.defaultSection.ok
        builder.setPositiveButton(label) { dialogInterface, i ->
            isShowingError = false
            closeFunction?.let { if(error.shouldCloseView) it() }.guard {
                Timber.e("View closure was requested but view haven't specified a CloseFunction in ViewErrorController constructor, ignoring.")
            }
        }
        builder.setOnDismissListener { isShowingError = false }
        if(!isShowingError) {
            isShowingError = true
            builder.show()
        }
    }

    companion object {
        var isShowingError = false
    }
}