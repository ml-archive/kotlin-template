package dk.eboks.app.presentation.ui.components.channels.content.ekey

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyComponentPresenter @Inject constructor(val appState: AppStateManager) : EkeyComponentContract.Presenter, BasePresenterImpl<EkeyComponentContract.View>() {

    override fun getKeys() {
        runAction { v ->
            v.showKeys(createMocks())
        }
    }

    private fun createMocks() : List<Ekey>{
        val keyList = mutableListOf<Ekey>()
        keyList.add(Login("test1@gmail.com", "gmailPW1", "Gmail", "Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet"))
        keyList.add(Pin("1234", "Visa", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null))
        keyList.add(Note("Summerhouse", "Lorem ipsum dolor sit amet"))
        keyList.add(Pin("4321", "MasterCard", null))

        return keyList
    }
}