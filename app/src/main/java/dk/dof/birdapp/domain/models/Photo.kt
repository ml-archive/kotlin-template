package dk.dof.birdapp.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Photo (
        var albumId : Int,
        var id : Int,
        var title : String,
        var url : String,
        var thumbnailUrl : String
) : Serializable