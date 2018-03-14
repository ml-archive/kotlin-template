package dk.eboks.app.presentation.ui.components.message.detail.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_notes_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NotesComponentFragment : BaseFragment(), NotesComponentContract.View {
    @Inject
    lateinit var presenter : NotesComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_notes_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        focusThiefV.requestFocus()
    }

    override fun setupTranslations() {
        notesTv.text = Translation.message.notes
        noteEt.hint = Translation.message.notePlaceholder
    }

    override fun updateView(message: Message) {

    }
}