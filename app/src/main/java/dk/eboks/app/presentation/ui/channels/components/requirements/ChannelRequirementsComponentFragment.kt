package dk.eboks.app.presentation.ui.channels.components.requirements

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.Requirement
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoActivity
import dk.eboks.app.util.Starter
import kotlinx.android.synthetic.main.fragment_channel_requirements_component.*
import kotlinx.android.synthetic.main.viewholder_channel_setting_boxrow.view.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelRequirementsComponentFragment : BaseFragment(), ChannelRequirementsComponentContract.View {

    @Inject
    lateinit var presenter: ChannelRequirementsComponentContract.Presenter

    //lateinit var currentItem: Channel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_requirements_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        cancelBtn.setOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        arguments?.getParcelable<Channel>(("channel"))?.let(presenter::setup)
    }


    override fun setupView(channel: Channel) {
        updateTranslation(channel.name)
        updateProfileBtn.setOnClickListener {
            activity.Starter().activity(MyInfoActivity::class.java).putExtra("channel", channel).start()
            activity.finish()
        }
    }

    override fun showUnverifiedRequirements(requirements: List<Requirement>) {
        requirementRowsRv.layoutManager = LinearLayoutManager(context)
        requirementRowsRv.adapter = RowAdapter(requirements)
    }

    private fun updateTranslation(channelName : String) {
        headerTextTv.text = Translation.channels.drawerHeaderText.replace("[channelname]", channelName)
    }

    inner class RowAdapter(requirements: List<Requirement>) : RecyclerView.Adapter<RowAdapter.ChannelRequirementViewHolder>() {
        val requirements: List<Requirement> = requirements

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChannelRequirementViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.viewholder_channel_setting_boxrow, parent, false)
            return ChannelRequirementViewHolder(view)
        }

        override fun getItemCount(): Int {
            return requirements.size
        }

        override fun onBindViewHolder(holder: ChannelRequirementViewHolder?, position: Int) {
            val requirement = requirements[position]
            holder?.bind(requirement)
        }

        inner class ChannelRequirementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(requirement: Requirement) {
                if (requirement.value.isNullOrBlank()) {
                    //if no value, set name on centeredHeader
                    itemView.centeredHeaderTv.text = requirement.name
                    itemView.missingTv.text = Translation.channels.missing
                    itemView.centeredHeaderTv.visibility = View.VISIBLE
                    itemView.missingTv.visibility = View.VISIBLE
                    // hiding the dual line and checkbox
                    itemView.headerTv.visibility = View.GONE
                    itemView.subHeaderTv.visibility = View.GONE
                    itemView.checkIv.visibility = View.GONE
                } else {
                    // set name on headerTv and value on subheader
                    itemView.headerTv.text = requirement.name
                    itemView.subHeaderTv.text = requirement.value
                    itemView.headerTv.visibility = View.VISIBLE
                    itemView.subHeaderTv.visibility = View.VISIBLE
                    itemView.centeredHeaderTv.visibility = View.GONE

                    // checkbox
                    if (requirement.verified == true) {
                        itemView.checkIv.visibility = View.VISIBLE
                        itemView.missingTv.visibility = View.GONE
                    } else {
                        // false
                        itemView.checkIv.visibility = View.GONE
                        itemView.missingTv.visibility = View.VISIBLE

                        if (requirement.value == null) {
                            itemView.missingTv.text = Translation.channels.missing
                        } else {
                            itemView.missingTv.text = Translation.channels.drawerNeedsVerification
                        }
                    }
                }
            }
        }

    }
}