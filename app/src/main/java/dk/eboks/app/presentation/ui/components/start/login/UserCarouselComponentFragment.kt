package dk.eboks.app.presentation.ui.components.start.login

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_carousel_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class UserCarouselComponentFragment : BaseFragment(), UserCarouselComponentContract.View {

    @Inject
    lateinit var presenter : UserCarouselComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_user_carousel_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupViewPager()
    }

    fun setupViewPager()
    {
        // Disable clip to padding
        viewPager.clipToPadding = false
        // set padding manually, the more you set the padding the more you see of prev & next page
        val density = resources.displayMetrics.density
        val sidepad = (density * 36f).toInt()
        viewPager.setPadding(sidepad, 0, sidepad, 0)
        // sets a margin b/w individual pages to ensure that there is a gap b/w them

        //val between = (density * 1f).toInt()
        viewPager.pageMargin = 0
        viewPager.adapter = UserPagerAdapter()
        viewPager.offscreenPageLimit = 10
    }

    override fun setupTranslations() {
        signupBtn.text = Translation.start.signupButton
    }

    inner class UserPagerAdapter : PagerAdapter()
    {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.viewholder_user_carousel, collection, false) as ViewGroup
            collection.addView(layout)
            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }
}