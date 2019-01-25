package dk.eboks.app.presentation.ui.notimplemented.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_coming_soon.*

class ComingSoonFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coming_soon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            comingSoonHeaderTv.text = it.getString(PARAM_HEADER)
            comingSoonMessageTv.text = it.getString(PARAM_MESSAGE)
        }
    }

    companion object {
        private const val PARAM_HEADER = "header"
        private const val PARAM_MESSAGE = "message"
        fun newInstance(header: String, message: String) = ComingSoonFragment().apply {
            putArg(PARAM_HEADER, header)
            putArg(PARAM_MESSAGE, message)
        }
    }
}