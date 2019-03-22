package dk.eboks.app.util

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import dk.eboks.app.domain.models.channel.Channel

interface FragmentArguments: Parcelable {
    fun toBundle() = bundleOf(BundleKeys.arguments to this)
}

fun Channel.toBundle() = bundleOf(BundleKeys.channel to this)

fun Bundle.getChannel() = getParcelable<Channel>(BundleKeys.channel)

object BundleKeys {
    const val arguments = "arguments"
    const val channel = "channel"
}