package dk.eboks.app.presentation.ui.message.sheet.components.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Message
import dk.eboks.app.presentation.ui.message.sheet.components.SheetComponentFragment
import kotlinx.android.synthetic.main.fragment_notes_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class NotesComponentFragment : SheetComponentFragment(), NotesComponentContract.View {
    @Inject
    lateinit var presenter : NotesComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_notes_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(this, lifecycle)
        focusThiefV.requestFocus()
    }

    override fun setupTranslations() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateView(message: Message) {

    }
}