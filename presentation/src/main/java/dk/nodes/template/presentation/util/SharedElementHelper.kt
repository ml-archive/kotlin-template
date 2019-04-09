package dk.nodes.template.presentation.util

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentTransaction
import java.lang.ref.WeakReference

class SharedElementHelper {
    private val sharedElementViews = mutableMapOf<WeakReference<View>, String?>()
    private val transitionData = mutableMapOf<String, String>()

    fun addSharedElementTransitionData(key: String, data: String) {
        transitionData[key] = data
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addSharedElement(view: View) {
        sharedElementViews[WeakReference(view)] = view.transitionName
    }

    fun addSharedElement(view: View, name: String) {
        sharedElementViews[WeakReference(view)] = name
    }

    fun applyToTransaction(tx: FragmentTransaction) {
        for ((viewRef, customTransitionName) in sharedElementViews) {
            viewRef.get()?.apply {
                tx.addSharedElement(this, customTransitionName!!)
            }
        }
    }

    fun applyToIntent(activity: Activity): Bundle? {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            *sharedElementViews.map { Pair(it.key.get(), it.value) }.toList().toTypedArray()
        ).toBundle()
    }

    fun isEmpty(): Boolean = sharedElementViews.isEmpty()

    fun hasExternalImageViews(): Boolean = sharedElementViews.any { it is ImageView }
}

fun sharedElements(vararg elements: kotlin.Pair<View, String>): SharedElementHelper {
    return SharedElementHelper().apply {
        elements.forEach {
            addSharedElement(it.first, it.second)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun sharedElements(vararg elements: View): SharedElementHelper {
    return SharedElementHelper().apply {
        elements.forEach {
            addSharedElement(it)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun sharedElements(elements: List<View>): SharedElementHelper {
    return sharedElements(*elements.toTypedArray())
}

fun SharedElementHelper.withTransitionData(vararg transitionDataPairs: kotlin.Pair<String, String>): SharedElementHelper {
    transitionDataPairs.forEach {
        addSharedElementTransitionData(it.first, it.second)
    }
    return this
}

fun SharedElementHelper.withTransitionData(data: Map<String, String>): SharedElementHelper {
    return withTransitionData(*(data.entries.map { kotlin.Pair(it.key, it.value) }.toTypedArray()))
}