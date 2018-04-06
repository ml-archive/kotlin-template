package dk.eboks.app.domain.models.home

import java.io.Serializable

/**
 * Created by thsk (not really, I was just to lazy to change it haha) on 16/02/2018.
 */
data class HomeContent(
    var control: Control,
    var version: Int
) : Serializable