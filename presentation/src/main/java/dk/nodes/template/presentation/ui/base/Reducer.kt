package dk.nodes.template.presentation.ui.base

typealias Reducer<S, C> = suspend (state: S, change: C) -> S