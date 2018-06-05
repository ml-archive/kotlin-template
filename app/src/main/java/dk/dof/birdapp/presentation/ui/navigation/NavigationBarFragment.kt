package dk.dof.birdapp.presentation.ui.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.dof.birdapp.R
import dk.dof.birdapp.R.id.mainNavigationBnv
import dk.dof.birdapp.presentation.base.BaseFragment
import dk.dof.birdapp.presentation.ui.birdbook.BirdbookComponentFragment
import dk.dof.birdapp.presentation.ui.map.MapComponentFragment
import dk.dof.birdapp.presentation.ui.whichbird.WhichBirdComponentFragment
import kotlinx.android.synthetic.main.fragment_navigation_component.*
import javax.inject.Inject

class NavigationBarFragment : BaseFragment(), NavigationBarContract.View {
    @Inject
    lateinit var presenter : NavigationBarContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_navigation_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupMainNavigation()
    }

    private fun setupMainNavigation()
    {
        mainNavigationBnv.inflateMenu(R.menu.main)
        var menu = mainNavigationBnv.menu
        //todo set translations
        menu.findItem(R.id.actionMap).title = "_kort"
        menu.findItem(R.id.actionWhatBird).title =  "_Hvilken fugl"
        menu.findItem(R.id.actionDofBirdbook).title = "_DOFs fuglebog"

//        mainNavigationBnv.disableShiftingMode()

        getBaseActivity()?.let {
            val menu_id = it.getNavigationMenuAction()
            if(menu_id != -1)
                currentMenuItem = menu_id
        }

        mainNavigationBnv.selectedItemId = currentMenuItem

        mainNavigationBnv.setOnNavigationItemSelectedListener { item ->
            when(item.itemId)
            {
                R.id.actionMap -> {
                    getBaseActivity()?.addFragmentOnTop(R.id.contentFl,MapComponentFragment(),false)
                    currentMenuItem = R.id.actionMap
                }
                R.id.actionWhatBird -> {
                    getBaseActivity()?.addFragmentOnTop(R.id.contentFl, WhichBirdComponentFragment(),false)
                    currentMenuItem = R.id.actionWhatBird
                }
                R.id.actionDofBirdbook -> {
                    getBaseActivity()?.addFragmentOnTop(R.id.contentFl, BirdbookComponentFragment(),false)
                    currentMenuItem = R.id.actionDofBirdbook
                }

                else -> { }
            }
            true
        }
    }

    companion object {
        var currentMenuItem = 0
    }

}