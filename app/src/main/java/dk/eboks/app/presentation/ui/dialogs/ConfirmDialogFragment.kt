package dk.eboks.app.presentation.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import dk.eboks.app.R

/**
 * Created by bison on 31/01/18.
 */
class ConfirmDialogFragment : DialogFragment() {
    lateinit var dialogInstance : Dialog
    var parentView : View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogInstance = Dialog(activity)
        dialogInstance.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogInstance.setContentView(root)
        dialogInstance.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogInstance.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        parentView = dialogInstance.window.decorView.rootView
        val size = (resources.displayMetrics.density * 32.0f).toInt()
        parentView?.setPadding(size, 0, size, 0)

        return dialogInstance
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View? = inflater?.inflate(R.layout.dialog_confirm, container)
        parentView?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parentView?.requestLayout()
        return view
    }

}