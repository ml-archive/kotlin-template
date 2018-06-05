package dk.dof.birdapp.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Post (
        var userId : Int,
        var id : Int,
        var title : String,
        var body : String
) : Serializable
