package dk.eboks.app.util

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import dk.eboks.app.BuildConfig

@GlideModule
class EboksAppGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        if (BuildConfig.DEBUG) {
            //builder.setLogLevel(Log.VERBOSE)
        }
    }
}