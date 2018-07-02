package dk.eboks.app.presentation.ui.components.senders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailActivity
import kotlinx.android.synthetic.main.fragment_segment_component.*

/**
 * Created by Christian on 3/20/2018.
 * @author   Christian
 * @since    3/20/2018.
 */
class SegmentComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_segment_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = arguments.getSerializable(CollectionContainer::class.simpleName) as CollectionContainer?
        container?.let{
            segmentTitleTv.text = it.description?.text?:""
            it.segment?.let { seg ->
                Glide.with(context)
                        .load(seg.image?.url)
                        .apply(RequestOptions()
                                .fallback(R.drawable.icon_72_senders_private)
                                .placeholder(R.drawable.icon_72_senders_private)
                        )
                        .into(segmentIv)
                segmentIv.clipToOutline = true

                segmentCv.setOnClickListener {
                    val i = Intent(context, SegmentDetailActivity::class.java)
                    i.putExtra(Segment::class.simpleName, seg)
                    startActivity(i)
                }
                segmentCatTv.text = seg.name
                segmentSignTv.text = when (seg.registered) {
                    0 -> Translation.senders.register
                    else -> Translation.senders.registered
                }
            }
        }
    }
}