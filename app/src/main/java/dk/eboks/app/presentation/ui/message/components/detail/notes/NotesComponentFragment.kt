package dk.eboks.app.presentation.ui.message.components.detail.notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notes_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NotesComponentFragment : BaseFragment(), NotesComponentContract.View, TextWatcher {
    @Inject
    lateinit var presenter: NotesComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_notes_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        focusThiefV.requestFocus()
        presenter.setup()
    }

    override fun updateView(message: Message) {
        noteEt.setText(message.note)
    }

    override fun onResume() {
        super.onResume()
        noteEt.addTextChangedListener(this)
    }

    override fun onPause() {
        noteEt.removeTextChangedListener(this)
        super.onPause()
    }

    val delayedRunnable = Runnable {
        val note = noteEt.text.toString().trim()
        presenter.saveNote(note)
    }

    override fun afterTextChanged(p0: Editable?) {
        mainHandler.removeCallbacks(delayedRunnable)
        mainHandler.postDelayed(delayedRunnable, BuildConfig.INPUT_VALIDATION_DELAY)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}