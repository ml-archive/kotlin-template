package dk.eboks.app.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import dk.eboks.app.presentation.injection.DaggerPresentationComponent
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : dk.eboks.app.presentation.base.BaseActivity(), dk.eboks.app.presentation.ui.main.MainContract.View {
    val component: dk.eboks.app.presentation.injection.PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(dk.eboks.app.presentation.injection.PresentationModule())
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
        initUiTestSamples()

        //textview = findViewById(R.id.textview) as TextView
        textview.text = dk.eboks.app.domain.models.Translation.defaultSection.settings
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
        textview.text = dk.eboks.app.domain.models.Translation.defaultSection.settings
    }

    //This is for the SampleActivityTest.class for Ui tests examples.
    private fun initUiTestSamples() {
        saveButton.setOnClickListener(View.OnClickListener {
            textview.text = edittext.text.trim()
        })

        buttonOpenDialog.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Hello Ui Test!")
                    .setMessage("This is an alert message. Let the UI test read it")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", null)
                    .show()
        })

        buttonOpenActivity.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, dk.eboks.app.presentation.ui.main.SampleActivity::class.java))
        })
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
