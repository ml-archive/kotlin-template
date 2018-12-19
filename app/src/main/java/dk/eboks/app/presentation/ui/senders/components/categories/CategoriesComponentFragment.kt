package dk.eboks.app.presentation.ui.senders.components.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryActivity
import kotlinx.android.synthetic.main.fragment_list_component.*
import kotlinx.android.synthetic.main.viewholder_title_subtitle.view.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentFragment : BaseFragment(), CategoriesComponentContract.View {


    @Inject
    lateinit var presenter : CategoriesComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val segment = arguments?.getParcelable<Segment>(Segment::class.simpleName)
        if (segment == null) {
            presenter.getCategories()
        }
        else {
            segment.categories?.let {
                showCategories(it)
            }
        }
    }

    override fun showCategories(categories: List<SenderCategory>) {
        listComponentContentLl.removeAllViews()

        categories.forEach {
            val v = inflator.inflate(R.layout.viewholder_title_subtitle, null)
            v.titleTv.text = it.name
            v.subTv.text = "${it.numberOfSenders}"
            v.setOnClickListener { v ->
                val i = Intent(context, BrowseCategoryActivity::class.java )
                i.putExtra(SenderCategory::class.simpleName, it)
                startActivity(i)
            }
            listComponentContentLl.addView(v)
        }
    }

    override fun showError(message: String) {

    }

}