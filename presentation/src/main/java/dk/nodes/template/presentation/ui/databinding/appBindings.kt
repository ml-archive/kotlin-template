package dk.nodes.template.presentation.ui.databinding

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import dk.nodes.template.models.Post

@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}

@BindingAdapter("posts")
fun setPosts(view: TextView, posts: List<Post>?) {
    posts?.joinToString { it.title + System.lineSeparator() }?.let(view::setText)
}