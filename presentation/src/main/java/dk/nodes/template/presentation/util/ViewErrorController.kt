package dk.nodes.template.presentation.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ActivityContext
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.domain.repositories.RepositoryException
import javax.inject.Inject

class ViewErrorController @Inject constructor(@ActivityContext val context: Context) {

    fun showErrorDialog(error: ViewError, cancelable: Boolean = true, dismissAction: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(error.title)
        builder.setMessage(error.message)
        builder.setPositiveButton(Translation.defaultSection.ok) { _, _ ->
            isShowingError = false
        }
        builder.setOnDismissListener {
            isShowingError = false
            dismissAction?.invoke()
        }
        if (!isShowingError) {
            isShowingError = true
            val dialog = builder.show()
            dialog.setCancelable(cancelable)
            dialog.setCanceledOnTouchOutside(cancelable)
        }
    }

    fun showErrorSnackbar(view: View, error: ViewError, showAction: Boolean = false, dismissAction: (() -> Unit)? = null) {
        val showLength = if (showAction) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
        val snackbar = Snackbar.make(view, error.message ?: Translation.error.errorRandom, showLength)
        if (showAction) {
            snackbar.setAction(Translation.defaultSection.ok) {
                isShowingError = false
                dismissAction?.invoke()
            }
        }
        if (!isShowingError) {
            isShowingError = true
            snackbar.show()
        }
    }

    companion object {
        var isShowingError = false

        fun mapThrowable(throwable: Throwable): ViewError {
            return when (throwable) {
                is RepositoryException -> {
                    when (throwable.code) {
                        401, 403 -> {
                            ViewError(
                                    title = Translation.error.errorTitle,
                                    message = Translation.error.authenticationError,
                                    code = -1
                            )
                        }
                        402, in 404..500 -> {
                            ViewError(
                                    title = Translation.error.errorTitle,
                                    message = Translation.error.unknownError,
                                    code = -1
                            )
                        }
                        in 500..600 -> {
                            ViewError(
                                    title = Translation.error.errorTitle,
                                    message = Translation.error.unknownError,
                                    code = -1
                            )
                        }
                        else -> {
                            ViewError(
                                    title = Translation.error.errorTitle,
                                    message = Translation.error.unknownError,
                                    code = -1
                            )
                        }
                    }
                }
                else -> {
                    ViewError(
                            title = Translation.error.errorTitle,
                            message = Translation.error.connectionError,
                            code = -1
                    )
                }
            }
        }
    }
}
