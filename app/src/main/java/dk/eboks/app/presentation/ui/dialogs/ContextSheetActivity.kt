package dk.eboks.app.presentation.ui.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.view.View
import dk.eboks.app.R
import kotlinx.android.synthetic.main.activity_context_sheet.*

/**
 * Created by bison on 05/02/18.
 */
class ContextSheetActivity : AppCompatActivity() {
    var sheetBehavior : BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_sheet)
        setupBottomSheet()
    }

    fun setupBottomSheet()
    {
        sheetBehavior = BottomSheetBehavior.from(contextSheet)
        sheetBehavior?.isHideable = true
        sheetBehavior?.peekHeight = (resources.displayMetrics.density * 64.0).toInt()
        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        contextSheet.post {
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}