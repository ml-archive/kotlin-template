package dk.eboks.app.presentation.ui.login.components

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentFragment
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserActivity
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.start.components.signup.NameMailComponentFragment
import dk.eboks.app.presentation.ui.start.components.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import kotlinx.android.synthetic.main.fragment_user_carousel_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class UserCarouselComponentFragment : BaseFragment(), UserCarouselComponentContract.View {

    @Inject
    lateinit var presenter: UserCarouselComponentContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_user_carousel_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupViewPager()
        signupBtn.setOnClickListener { getBaseActivity()?.addFragmentOnTop(R.id.containerFl, NameMailComponentFragment(), true) }
        if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
            /*
            debugCreateBtn.visibility = View.VISIBLE
            debugCreateBtn.setOnClickListener {
                (activity as StartActivity).startMain()
            }
            */
            debugOptionsTv.visibility = View.VISIBLE
            debugOptionsTv.setOnClickListener {
                getBaseActivity()?.openComponentDrawer(DebugOptionsComponentFragment::class.java)
            }
            debugCreateUserTv.visibility = View.VISIBLE
            debugCreateUserTv.setOnClickListener {
                activity.startActivity(Intent(activity, DebugUserActivity::class.java))
            }
        }
        (activity as StartActivity).enableFragmentCheapFades()
    }

    override fun onResume() {
        super.onResume()
        logoIv.setImageResource(Config.getLogoResourceId())
        presenter.requestUsers()
    }

    fun setupViewPager() {
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

    override fun showUsers(users: MutableList<Pair<User, UserSettings>>) {
        viewPager.adapter = UserPagerAdapter(users)
        if (users.isEmpty()) {
            presenter.clearSelectedUser()
            getBaseActivity()?.setRootFragment(R.id.containerFl, WelcomeComponentFragment())
        }
    }

    override fun setSelectedUser(user: User) {
        val pos = (viewPager.adapter as UserPagerAdapter).users.indexOfFirst { it.first == user }
        Timber.i("setSelectedUser position: $pos, ${user.name}")
        if(pos >= 0) {
            viewPager.setCurrentItem(pos, true)
        }
    }

    override fun openLogin() {
        val frag = LoginComponentFragment()
        getBaseActivity()?.addFragmentOnTop(R.id.containerFl, frag, true)
    }

    private fun showDeleteDialog(user: User) {
        AlertDialog.Builder(activity)
                .setTitle(Translation.start.confimRemoveUserTitle)
                .setMessage(Translation.start.confirmRemoveUserMessage)
                .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                    //(viewPager.adapter as UserPagerAdapter).users.remove(user)
                    //viewPager.adapter.notifyDataSetChanged()
                    presenter.deleteUser(user)
                }
                .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->

                }
                .show()
    }

    inner class UserPagerAdapter(val users: MutableList<Pair<User, UserSettings>>) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(context)
            if (position < users.size) {
                val user = users[position].first
                val settings = users[position].second
                val v = inflater.inflate(R.layout.viewholder_user_carousel, collection, false) as ViewGroup

                if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
                    var info = ""
                    if (settings.lastLoginProviderId != null)
                        info += "${settings.lastLoginProviderId}\n"
                    if (user.verified)
                        info += "verified\n"
                    if (settings.hasFingerprint)
                        info += "fingerprint\n"
                    if (info.isNotBlank()) {
                        v.findViewById<TextView>(R.id.debugInfoTv)?.let {
                            it.text = info
                            it.visibility = View.VISIBLE
                        }
                    }
                }

                v.findViewById<TextView>(R.id.nameTv)?.text = user.name

                collection.addView(v)
                v.findViewById<LinearLayout>(R.id.clickLl)?.let {
                    it.setOnClickListener {
                        presenter.login(user)
                    }
                    if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
                        it.setOnLongClickListener {
                            DebugUserPresenter.editUser = user
                            activity.startActivity(Intent(activity, DebugUserActivity::class.java))
                            true
                        }
                    }
                }

                if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
                    v.findViewById<TextView>(R.id.hintTv)?.visibility = View.VISIBLE
                }

                v.findViewById<ImageView>(R.id.deleteIv)?.setOnClickListener {
                    showDeleteDialog(user)
                }
                return v
            } else {
                val v = inflater.inflate(R.layout.viewholder_add_user_carousel, collection, false) as ViewGroup
                v.findViewById<TextView>(R.id.nameTv)?.text = Translation.start.addNewUser
                v.findViewById<LinearLayout>(R.id.addUserLl)?.setOnClickListener {
                    presenter.addAnotherUser()
                }
                collection.addView(v)
                return v
            }
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return users.size + 1
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }
}