package dk.eboks.app.presentation.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R


/**
 * Created by bison on 05/02/18.
 */
class ContextSheetDialogFragment : BottomSheetDialogFragment() {
    var contentView : View? = null

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }

        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater?.inflate(R.layout.fragment_context_sheet, container)
        return contentView
    }

}