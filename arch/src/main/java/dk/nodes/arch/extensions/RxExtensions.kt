package dk.nodes.arch.extensions

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

fun <T> Observable<T>.toFlowable() = toFlowable(BackpressureStrategy.LATEST)!!