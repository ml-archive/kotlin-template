package dk.nodes.template.main

import dk.nodes.template.models.Post
import dk.nodes.template.presentation.util.Event

data class MainActivityViewState(
    val posts: List<Post> = emptyList(),
    val errorMessage: Event<String>? = null,
    val isLoading: Boolean = false
)