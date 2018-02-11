package dk.eboks.app.presentation.base

import android.support.v4.app.Fragment
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.nodes.arch.presentation.base.BaseView

abstract class BaseFragment : Fragment(), BaseView {
    protected val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((activity.application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }

    override fun onStart() {
        super.onStart()
        setupTranslations()
    }

}