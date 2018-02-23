package dk.eboks.app.presentation.ui.components.channels.list

import android.graphics.Color
import android.media.tv.TvContract
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Description
import dk.eboks.app.domain.models.Status
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.*
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_list_component.*
import kotlinx.android.synthetic.main.viewholder_channel_cards.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by bison on 09-02-2018.
 */
class ChannelListComponentFragment : BaseFragment(), ChannelListComponentContract.View {

    var cards: MutableList<Channel> = ArrayList()

    @Inject
    lateinit var presenter: ChannelListComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_list_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupRecyclerView()

        setupMockedData()
    }

    private fun setupMockedData() {
        // card0 - Dummy card for the top textview - should be changed later
        var status0 = Status(false, null, null, 0, Date());
        var desecription0 = Description("desription1")
        var logo0 = Image("https://is1-ssl.mzstatic.com/image/thumb/Purple62/v4/1c/df/67/1cdf6719-e05f-bb7a-3676-69811b41d168/mzl.gxqksxgx.png/1200x630bb.jpg", "text1", null)
        var image0 = Image("https://www.mockupworld.co/wp-content/uploads/edd/2016/04/iphone-transparent-free-mockup-1000x566.jpg", "text1", null)
        var color0 = ChannelColor("rbg", "#10a5c5")
        var requirements0: Array<Requirement>? = null
        requirements0 = arrayOf(Requirement("nameRequirement", "value", RequirementType.NAME))

        var card0 = Channel(-1, "name1", "payoff1", desecription0, status0, logo0, image0, color0, requirements0, false, false);

        // card 1
        var status = Status(false, null, null, 0, Date());
        var desecription1 = Description("desription1")
        var logo = Image("https://is1-ssl.mzstatic.com/image/thumb/Purple62/v4/1c/df/67/1cdf6719-e05f-bb7a-3676-69811b41d168/mzl.gxqksxgx.png/1200x630bb.jpg", "text1", null)
        var image = Image("https://www.mockupworld.co/wp-content/uploads/edd/2016/04/iphone-transparent-free-mockup-1000x566.jpg", "text1", null)
        var color = ChannelColor("rbg", "#BF10a5c5")
        var requirements: Array<Requirement>? = null
        requirements = arrayOf(Requirement("nameRequirement", "value", RequirementType.NAME))

        var card1 = Channel(1, "Mecenat", "Nøglen til et rigere studieliv", desecription1, status, logo, image, color, requirements, false, false);

        // card 2
        var status2 = Status(false, null, null, 0, Date());
        var desecription2 = Description("desription2")
        var logo2 = Image("http://hareskovskole.skoleporten.dk/sp/resource/image/9cc24af6-5339-4eeb-97f5-11278b6859ac?width=573&height=200", "text2", null)
        var image2 = Image("http://hareskovskole.skoleporten.dk/sp/resource/image/9cc24af6-5339-4eeb-97f5-11278b6859ac?width=573&height=200", "text2", null)
        var color2 = ChannelColor("rbg", "#BFFF0000")
        var requirements2: Array<Requirement>? = null
        requirements2 = arrayOf(Requirement("nameRequirement", "value", RequirementType.NAME))

        var card2 = Channel(2, "name2", "payoff2", desecription2, status2, logo2, image2, color2, requirements2, false, false);

        // card 3
        var status3 = Status(false, null, null, 0, Date());
        var desecription3 = Description("desription3")
        var logo3 = Image("http://hareskovskole.skoleporten.dk/sp/resource/image/9cc24af6-5339-4eeb-97f5-11278b6859ac?width=573&height=200", "text3", null)
        var image3 = Image("http://hareskovskole.skoleporten.dk/sp/resource/image/9cc24af6-5339-4eeb-97f5-11278b6859ac?width=573&height=200", "text3", null)
        var color3 = ChannelColor("rbg", "#BF0000FF")
        var requirements3: Array<Requirement>? = null
        requirements3 = arrayOf(Requirement("nameRequirement", "value", RequirementType.NAME))

        var card3 = Channel(3, "name3", "payoff3", desecription3, status3, logo3, image3, color3, requirements3, false, false);

        cards.add(card0)
        cards.add(card1)
        cards.add(card2)
        cards.add(card3)
        channelRv.adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        channelRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        channelRv.adapter = ChannelAdapter()
    }


    inner class ChannelAdapter : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

        inner class ChannelViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            //header
            val headerTv = root.findViewById<TextView>(R.id.headerTv)
            //cards

            val cardContainerCv = root.findViewById<CardView>(R.id.cardContainerCv)
            val backgroundIv = root.findViewById<ImageView>(R.id.backgroundIv)
            val backgroundColorLl = root.findViewById<LinearLayout>(R.id.backgroundColorLl)
            val headlineTv = root.findViewById<TextView>(R.id.headerTv)
            val logoIv = root.findViewById<ImageView>(R.id.logoIv)
            val nameTv = root.findViewById<TextView>(R.id.nameTv)
            val button = root.findViewById<Button>(R.id.button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_channel_cards, parent, false)
            val vh = ChannelViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return cards.size
        }

        override fun onBindViewHolder(holder: ChannelViewHolder?, position: Int) {
            var currentCard = cards[position]

            if (currentCard.id == -1) {
                holder?.headerTv?.visibility = View.VISIBLE
                holder?.cardContainerCv?.visibility = View.GONE
                holder?.headerTv?.setText(Translation.channels.channelsHeader)
            } else {

                if (currentCard.background != null) {
                    holder?.backgroundIv?.let {
                        val requestOptions = RequestOptions()
                                .transform(RoundedCorners(20))

                        Glide.with(context)
                                .load(currentCard?.image?.url)
                                .apply(requestOptions)
                                .into(it)
                    }
                }

                if (currentCard.logo != null) {
                    holder?.logoIv?.let {
                        Glide.with(context).load(currentCard?.logo?.url).into(it)
                    }
                }

                var backgroundcolor = currentCard.background?.rgba
                holder?.backgroundColorLl?.background?.setTint(Color.parseColor(backgroundcolor))
                holder?.headlineTv?.setText(currentCard.payoff)


                holder?.nameTv?.setText(currentCard.name)
                // todo get translations and confirm the logic is correct
                if (currentCard.installed == true) {
                    holder?.button?.setText(Translation.channels.open)
                } else {
                    holder?.button?.setText(Translation.channels.install)
                }

            }
        }
    }


    override fun showChannels(channels: List<Channel>) {
        // TODO show the data
    }

    override fun setupTranslations() {

    }
}