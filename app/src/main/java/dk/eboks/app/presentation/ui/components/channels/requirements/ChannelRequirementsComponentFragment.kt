package dk.eboks.app.presentation.ui.components.channels.requirements

import android.content.Intent
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
import dk.eboks.app.domain.models.channel.RequirementType
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.screens.profile.myinfo.MyInfoActivity
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_channel_requirements_component.*
import kotlinx.android.synthetic.main.viewholder_channel_setting_boxrow.*
import kotlinx.android.synthetic.main.viewholder_channel_setting_boxrow.view.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelRequirementsComponentFragment : BaseFragment(), ChannelRequirementsComponentContract.View {

    @Inject
    lateinit var presenter: ChannelRequirementsComponentContract.Presenter

    lateinit var currentItem: Channel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_requirements_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        updateProfileBtn.setOnClickListener {
            var intent = Intent(context, MyInfoActivity::class.java)
            intent.putExtra("channel",currentItem)
            startActivity(intent)
        }
        cancelBtn.setOnClickListener {
            // cancel clicked
            getBaseActivity()?.onBackPressed()
        }
        requirementRowsRv.layoutManager = LinearLayoutManager(context)
        var item = arguments?.getSerializable(("channel"))
        item?.let {
            currentItem = item as Channel
            currentItem?.requirements?.let {
                requirementRowsRv.adapter = RowAdapter(it)
            }
        }
        updateTranslation()
    }

    private fun updateTranslation() {
        headerTextTv.text = Translation.channels.drawerHeaderText.replace("[channelname]",currentItem.name)
    }



    inner class RowAdapter(requirements: Array<Requirement>) : RecyclerView.Adapter<RowAdapter.ChannelRequirementViewHolder>() {
        val requirements: Array<Requirement> = requirements

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