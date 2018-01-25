package dk.eboks.app.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.disableShiftingMode
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_navigation_view.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: dk.eboks.app.presentation.ui.main.MainContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_main)
        mainNavigationBnv.inflateMenu(R.menu.main)
        mainNavigationBnv.disableShiftingMode()
    }

    override fun setupTranslations() {

    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MainActivity)
    }


    override fun showPosts(posts: List<dk.eboks.app.domain.models.Post>) {
        for (post in posts) {
            Log.d("debug", post.toString())
        }
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
