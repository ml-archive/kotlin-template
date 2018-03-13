package dk.eboks.app.presentation.ui.screens.senders.overview

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class BrowseCategoryPresenter(val appStateManager: AppStateManager) : BrowseCategoryContract.Presenter, BasePresenterImpl<BrowseCategoryContract.View>() {

    init {
    }

    override fun loadSenders(senderId: Long) {
        val senders = ArrayList<Sender>()

        for(i in 0..60) {
            val r = Math.random()*25 + 65
            val s = Sender(i.toLong(), "${r.toInt().toChar()}senderName$i", 0, "https://qu6oa42ax6a2pyq2c11ozwvm-wpengine.netdna-ssl.com/wp-content/uploads/2017/10/nodes-logo-2017.png")
            senders.add(s)
        }
        runAction { v ->
            v.showSenders(senders)
        }
    }
}