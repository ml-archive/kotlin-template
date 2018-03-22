package dk.eboks.app.presentation.base

import android.content.Context
import android.support.v7.app.AlertDialog
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.util.guard
import timber.log.Timber

/**
 * Created by bison on 22/03/2018.
 */
class DefaultErrorHandler(val context: Context) {

    fun showError(error : ServerError)
    {
        val builder = AlertDialog.Builder(context)
        error.description?.let { description ->
            description.title?.let { builder.setTitle(it) }
            builder.setMessage(description.text)
        }.guard {
            Timber.e("Not possible to show error since description is null, showing generic")
            builder.setTitle(Translation.error.errorTitle10100)
            builder.setMessage(Translation.error.errorMessage10100)
        }
        builder.setPositiveButton(Translation.defaultSection.ok, { dialogInterface, i ->

        })
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