package dk.nodes.template.ui.main

import dk.nodes.template.api.Post

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val api: dk.nodes.template.api.ApiProxy) : dk.nodes.template.mvp.MvpBasePresenter<MainMvpView>() {
    init {
    }

    override fun attachView(view: MainMvpView) {
        super.attachView(view)
        android.util.Log.e("debug", "attachView")

        loadPosts({ posts ->
            //if (isViewAttached)
                //view?.showPosts(posts)
        }, { errorCode, msg ->
            if (isViewAttached) {
                // showerror
            }
        })


    }

    override fun detachView() {
        super.detachView()
        android.util.Log.e("debug", "detachView")
    }

    fun loadPosts(onSuccess : (posts : List<dk.nodes.template.api.Post>) -> Unit, onFailure : (errorCode : Int, msg : String) -> Unit)
    {
        api.getPosts().enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onFailure(call: retrofit2.Call<List<Post>>?, t: Throwable?) {
                android.util.Log.e("debug", "onFailure")
                onFailure(-1, t?.message ?: "Unknown error")
            }

            override fun onResponse(call: retrofit2.Call<List<Post>>?, response: retrofit2.Response<List<Post>>?) {
                android.util.Log.e("debug", "got result")
                val posts : List<dk.nodes.template.api.Post> = response?.body() ?: throw RuntimeException("Body could not be read")
                if(response.isSuccessful)
                {
                    onSuccess(posts)
                }
                else
                {
                    onFailure(500, response.message())
                }
            }

        })
    }
}