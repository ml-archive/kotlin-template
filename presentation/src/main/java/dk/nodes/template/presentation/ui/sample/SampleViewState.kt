package dk.nodes.template.presentation.ui.sample

import dk.nodes.template.models.Post
import dk.nodes.template.presentation.util.SingleEvent

data class SampleViewState(
    val posts: List<Post> = emptyList(),
    val errorMessage: SingleEvent<String>? = null,
    val isLoading: Boolean = false
)