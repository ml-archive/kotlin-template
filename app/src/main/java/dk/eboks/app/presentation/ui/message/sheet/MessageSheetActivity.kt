package dk.eboks.app.presentation.ui.message.sheet

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.dialogs.ContextSheetActivity
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MessageSheetActivity : ContextSheetActivity(), MessageSheetContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject
    lateinit var presenter: MessageSheetContract.Presenter

    fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentSheet(R.layout.sheet_message)
        injectDependencies()
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun setupTranslations() {
        
    }
}