package dk.eboks.app.presentation.ui.components.start.welcome

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.start.login.UserCarouselComponentFragment
import kotlinx.android.synthetic.main.fragment_splash_component.*

/**
 * Created by bison on 09-02-2018.
 */
class SplashComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_splash_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupTranslations() {
    }

    fun transitionToWelcomeFragment()
    {
        val move = TransitionInflater.from(activity).inflateTransition(android.R.transition.move)
        val fade = TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_bottom)

        val fragment = WelcomeComponentFragment()

        sharedElementReturnTransition = move
        exitTransition = fade

        fragment.sharedElementEnterTransition = move
        fragment.enterTransition = fade
        //exitT.excludeTarget(R.id.start_root, true)
        //exitT.addTarget(R.id.start_desc_tv)


        //fragmentManager.popBackStack(BaseActivity.backStackRootTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.beginTransaction()
                .replace(R.id.containerFl, fragment)
                .addToBackStack(BaseActivity.backStackRootTag)
                .addSharedElement(logoIv, "eboksLogoTransition")
                .commit()

    }

    fun transitionToUserCarouselFragment()
    {
        val move = TransitionInflater.from(activity).inflateTransition(android.R.transition.move)
        val fade = TransitionInflater.from(activity).inflateTransition(android.R.transition.fade)

        val fragment = UserCarouselComponentFragment()

        sharedElementReturnTransition = move
        exitTransition = fade

        fragment.sharedElementEnterTransition = move
        fragment.enterTransition = fade
        //exitT.excludeTarget(R.id.start_root, true)
        //exitT.addTarget(R.id.start_desc_tv)


        //fragmentManager.popBackStack(BaseActivity.backStackRootTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.beginTransaction()
                .replace(R.id.containerFl, fragment)
                .addToBackStack(BaseActivity.backStackRootTag)
                .addSharedElement(logoIv, "eboksLogoTransition")
                .commit()

    }
}