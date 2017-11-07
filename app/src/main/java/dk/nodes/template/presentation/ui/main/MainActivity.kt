package dk.nodes.template.presentation.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import dk.nodes.template.domain.models.Post
import dk.nodes.template.App
import dk.nodes.template.R
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.presentation.injection.DaggerPresentationComponent
import dk.nodes.template.presentation.injection.PresentationComponent
import dk.nodes.template.presentation.injection.PresentationModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter : MainContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)

        //textTv = findViewById(R.id.text) as TextView
        textTv.text = Translation.defaultSection.settings
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


    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        NStack.translate(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun showPosts(posts: List<Post>) {
        for(post in posts)
        {
            Log.d("debug", post.toString())
        }
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
