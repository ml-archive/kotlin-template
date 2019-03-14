package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.models.Post
import dk.nodes.template.util.Event

data class MainActivityViewState(
    val posts: List<Post> = emptyList(),
    val errorMessage: Event<String>? = null,
    val isLoading: Boolean = false
)