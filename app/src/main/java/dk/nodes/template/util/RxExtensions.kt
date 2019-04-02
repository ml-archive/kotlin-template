package dk.nodes.template.util

import io.reactivex.BackpressureStrategy
import io.reactivex.Observable

fun <T> Observable<T>.toFlowable() = toFlowable(BackpressureStrategy.LATEST)!!