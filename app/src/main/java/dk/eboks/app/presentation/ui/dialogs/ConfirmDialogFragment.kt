package dk.eboks.app.presentation.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import dk.eboks.app.R

/**
 * Created by bison on 31/01/18.
 */
class ConfirmDialogFragment : DialogFragment() {
    private lateinit var dialogInstance: Dialog
    private var parentView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val root = RelativeLayout(requireContext())
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogInstance = Dialog(requireContext())
        dialogInstance.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogInstance.setContentView(root)
        dialogInstance.window?.run {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            parentView = decorView.rootView
        }

        val size = (resources.displayMetrics.density * 32.0f).toInt()
        parentView?.setPadding(size, 0, size, 0)

        return dialogInstance
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.dialog_confirm, container)
        parentView?.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        parentView?.requestLayout()
        return view
    }
}