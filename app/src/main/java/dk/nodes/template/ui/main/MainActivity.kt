package dk.nodes.template.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dk.bison.wt.R
import dk.nodes.template.api.Post
import dk.nodes.kstack.KStack
import dk.nodes.kstack.UpdateType
import dk.nodes.template.App
import dk.nodes.template.Translation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainMvpView {
    private lateinit var presenter : MainPresenter

    //@Translate("defaultSection.cancel") var textTv : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //textTv = findViewById(R.id.text) as TextView
        textTv.text = Translation.defaultSection.settings
        KStack.translate(this@MainActivity)

        presenter = MainPresenter(App.apiProxy())

        KStack.appOpen({ success -> Log.e("debug", "appopen success = $success") })

        KStack.versionControl(this@MainActivity, { type, builder ->
            when (type) {
                UpdateType.UPDATE -> builder?.show()
                UpdateType.FORCE_UPDATE -> {
                    builder?.setOnDismissListener { finish() }
                    builder?.show()
                }
                else -> {
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        KStack.translate(this@MainActivity)
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
}
