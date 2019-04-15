package dk.nodes.template.presentation.ui.main

import dk.nodes.template.models.Post
import dk.nodes.template.presentation.util.SingleEvent

data class MainActivityViewState(
    val posts: List<Post> = emptyList(),
    val errorMessage: SingleEvent<String>? = null,
    val isLoading: Boolean = false
)