package dk.eboks.app.presentation.ui.message.components.viewers.image

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ImageViewComponentPresenter @Inject constructor(val appState: AppStateManager) : ImageViewComponentContract.Presenter, BasePresenterImpl<ImageViewComponentContract.View>() {

    init {

    }

    override fun setup(uriString : String?) {
        uriString?.let {
            runAction { v->
                v.showImageURI(uriString)
            }
        }.guard {
            appState.state?.currentViewerFileName?.let { filename->
                runAction { v->
                    v.showImage(filename)
                }
            }
        }
    }
}