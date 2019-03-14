package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.Post
import dk.nodes.template.util.Event

data class MainActivityViewState(
    val posts: List<dk.nodes.template.domain.Post> = emptyList(),
    val errorMessage: Event<String>? = null,
    val isLoading: Boolean = false
)