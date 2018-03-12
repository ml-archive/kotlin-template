package dk.eboks.app.presentation.ui.components.senders.categories

import android.content.Context
import android.os.Bundle
import android.support.v4.view.LayoutInflaterCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_senders_categories_component.*
import kotlinx.android.synthetic.main.fragment_signup_terms_component.view.*
import kotlinx.android.synthetic.main.viewholder_sender_category.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 * Copied by chnt on 12-03-2018
 */
class CategoriesComponentFragment : BaseFragment(), CategoriesComponentContract.View {

    @Inject
    lateinit var presenter : CategoriesComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_senders_categories_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showCategories(categories: List<SenderCategory>) {
        categoriesLl.removeAllViews()
        categories.forEach {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_sender_category, null)
            v.categoryNameTv.text = it.name
            v.categoryCountTv.text = "${it.numberOfSenders}"
            categoriesLl.addView(v)
        }
    }

    override fun setupTranslations() {

    }
}