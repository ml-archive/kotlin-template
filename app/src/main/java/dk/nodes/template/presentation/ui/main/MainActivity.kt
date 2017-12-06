package dk.nodes.template.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import dk.nodes.template.App
import dk.nodes.template.R
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.injection.components.DaggerPresentationComponent
import dk.nodes.template.presentation.base.BaseActivity
import dk.nodes.template.injection.components.PresentationComponent
import dk.nodes.template.injection.modules.PresentationModule
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: MainContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUiTestSamples()

        //textview = findViewById(R.id.textview) as TextView
        textview.text = Translation.defaultSection.settings
        NStack.translate(this@MainActivity)

        NStack.appOpen({ success -> Log.e("debug", "appopen success = $success") })

        NStack.versionControl(this@MainActivity, { type, builder ->
            when (type) {
                UpdateType.UPDATE -> builder?.show()
                UpdateType.FORCE_UPDATE -> {
                    //builder?.setOnDismissListener { finish() }
                    //builder?.show()
                }
                else -> {
                }
            }
        })
    }


    override fun setupTranslations() {
        textview.text = Translation.defaultSection.settings
    }

    //This is for the SampleActivityTest.class for Ui tests examples.
    private fun initUiTestSamples() {
        saveButton.setOnClickListener({
            textview.text = edittext.text.trim()
        })

        buttonOpenDialog.setOnClickListener({
            AlertDialog.Builder(this)
                    .setTitle("Hello Ui Test!")
                    .setMessage("This is an alert message. Let the UI test read it")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", null)
                    .show()
        })

        buttonOpenActivity.setOnClickListener({
            startActivity(Intent(this, SampleActivity::class.java))
        })
    }


    override fun onResume() {
        super.onResume()
        NStack.translate(this@MainActivity)
    }


    override fun showPosts(posts: List<Post>) {
        for (post in posts) {
            Timber.e(post.toString())
        }
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }
}
