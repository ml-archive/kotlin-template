package dk.nodes.template

import android.app.Application
import android.util.Log
import dk.nodes.template.network.ApiProxy
import dk.nodes.template.network.makeApiProxy
import dk.nodes.kstack.KStack

/**
 * Created by bison on 20-05-2017.
 */
class App : Application()
{
    companion object {
        private lateinit var proxy : ApiProxy
        private lateinit var _instance : App

        fun apiProxy() : ApiProxy
        {
            return proxy
        }

        fun instance() : App
        {
            return _instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        proxy = makeApiProxy()

        KStack.setLogFunction { tag, msg -> Log.e(tag, msg) }
        KStack.init(this, "BmZHmoKuU99A5ZnOByOiRxMVSmAWC2yBz3OW", "yw9go00oCWt6zPhfbdjRYXiHRWmkQZQSuRke", true)
        KStack.setTranslationClass(Translation::class.java)

    }
}