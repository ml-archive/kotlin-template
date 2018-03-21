package dk.eboks.app.presentation.ui.components.senders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_segment_component.*

/**
 * Created by Christian on 3/20/2018.
 * @author   Christian
 * @since    3/20/2018.
 */
class SegmentComponentFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_segment_component, container, false)
        return rootView
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seg = arguments.getSerializable(Segment::class.simpleName) as Segment?
        seg?.let {
            //            Glide.with(context).load(segment.image.url).into(segmentIv)
            segmentTitleTv.text = it.name
            segmentCv.setOnClickListener {
                Toast.makeText(context, "TODO: open segment", Toast.LENGTH_SHORT).show()
            }
            segmentCatTv.text = it.type
            segmentSignTv.text = "Nstack register" // TODO
        }
    }

    override fun setupTranslations() {

    }
}