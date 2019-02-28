package dk.eboks.app.presentation.ui.senders.components.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dk.eboks.app.R
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryActivity
import dk.eboks.app.senders.presentation.ui.components.categories.CategoriesComponentContract
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_list_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentFragment : BaseFragment(), CategoriesComponentContract.View,
    CategoriesAdapter.Callback {
    @Inject lateinit var presenter: CategoriesComponentContract.Presenter

    private lateinit var adapter: CategoriesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        adapter = CategoriesAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        presenter.onViewCreated(this, lifecycle)
        arguments?.getParcelable<Segment>(Segment::class.simpleName)
            ?.categories?.let(::showCategories).guard {
            presenter.getCategories()
        }
    }

    override fun showCategories(categories: List<SenderCategory>) {
        adapter.setData(categories)
    }

    override fun onCategoryClick(category: SenderCategory) {
        val i = Intent(context, BrowseCategoryActivity::class.java)
        i.putExtra(SenderCategory::class.simpleName, category)
        startActivity(i)
    }

    override fun showError(message: String) {
    }
}