package dk.nodes.template.presentation.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dk.nodes.template.presentation.util.ViewErrorController
import javax.inject.Inject

abstract class BaseFragment : Fragment {

    constructor()
    constructor(@LayoutRes resId: Int) : super(resId)

    @Inject
    lateinit var defaultErrorController: dagger.Lazy<ViewErrorController>
}