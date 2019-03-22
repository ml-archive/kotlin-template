package dk.eboks.app.util

import android.os.Bundle
import androidx.core.os.bundleOf
import dk.eboks.app.domain.models.channel.Channel

interface FragmentArguments {
    fun toBundle(): Bundle {
        return bundleOf(BundleKeys.arguments to this)
    }
}

fun Channel.toBundle() = bundleOf(BundleKeys.channel to this)

fun Bundle.getChannel() = getParcelable<Channel>(BundleKeys.channel)

object BundleKeys {
    const val arguments = "arguments"
    const val channel = "channel"
}