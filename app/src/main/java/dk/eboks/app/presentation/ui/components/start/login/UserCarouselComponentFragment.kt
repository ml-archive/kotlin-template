package dk.eboks.app.presentation.ui.components.start.login

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.start.signup.NameMailComponentFragment
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import kotlinx.android.synthetic.main.fragment_user_carousel_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class UserCarouselComponentFragment : BaseFragment(), UserCarouselComponentContract.View {

    @Inject
    lateinit var presenter : UserCarouselComponentContract.Presenter

    @Inject
    lateinit var formatter : EboksFormatter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_user_carousel_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupViewPager()
        signupBtn.setOnClickListener { (activity as StartActivity).replaceFragment(NameMailComponentFragment()) }
        if(BuildConfig.DEBUG) {
            debugCreateBtn.visibility = View.VISIBLE
            debugCreateBtn.setOnClickListener {
                (activity as StartActivity).startMain()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.requestUsers()
        signupBtn.post({
            fragmentManager.findFragmentByTag(WelcomeComponentFragment::class.java.simpleName)?.let { frag->
                Timber.e("Found frag, removing")
                fragmentManager.beginTransaction().remove(frag).commitNowAllowingStateLoss()
                //fragmentManager.beginTransaction().replace(R.id.containerFl, UserCarouselComponentFragment(), UserCarouselComponentFragment::class.java.simpleName).commitNow()
            }
        })

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
        viewPager.offscreenPageLimit = 10
    }

    override fun setupTranslations() {
        signupBtn.text = Translation.start.signupButton
    }

    override fun showUsers(users: MutableList<User>) {
        viewPager.adapter = UserPagerAdapter(users)
    }

    override fun openLogin() {
        val frag = LoginComponentFragment()
        (activity as StartActivity).replaceFragment(frag)
    }

    inner class UserPagerAdapter(val users: List<User>) : PagerAdapter()
    {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(context)
            if(position < users.size) {
                val user = users[position]
                val v = inflater.inflate(R.layout.viewholder_user_carousel, collection, false) as ViewGroup

                var name = ""

                if(user.email != null) {
                    name = users[position].email!!
                }
                else if(user.cpr != null)
                {
                    name = formatter.formatCpr(users[position].cpr!!)
                }

                if(BuildConfig.DEBUG)
                    name += " (${if(user.verified) "Verified" else "Non-verified"})"

                v.findViewById<TextView>(R.id.nameTv)?.text = name

                collection.addView(v)
                v.findViewById<LinearLayout>(R.id.clickLl)?.setOnClickListener {
                    presenter.login(users[position])
                }
                return v
            }
            else if(position == users.size)
            {
                val v = inflater.inflate(R.layout.viewholder_add_user_carousel, collection, false) as ViewGroup
                v.findViewById<TextView>(R.id.nameTv)?.text = Translation.start.addNewUser
                v.findViewById<LinearLayout>(R.id.addUserLl)?.setOnClickListener {
                    presenter.addAnotherUser()
                }
                collection.addView(v)
                return v
            }
            else
            {
                if(BuildConfig.DEBUG) {
                    val v = inflater.inflate(R.layout.viewholder_add_user_carousel, collection, false) as ViewGroup
                    v.findViewById<TextView>(R.id.nameTv)?.text = "Create User (DEBUG)"
                    v.findViewById<LinearLayout>(R.id.addUserLl)?.setOnClickListener {
                        presenter.addAnotherUser()
                    }
                    collection.addView(v)
                    return v
                }
            }
            return Any()
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return if(!BuildConfig.DEBUG) users.size + 1 else users.size + 2
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }
}