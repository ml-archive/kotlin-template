package dk.nodes.template.api

import java.io.Serializable

/**
 * Created by bison on 20-05-2017.
 */
data class Post (
    var userId : Int,
    var id : Int,
    var title : String,
    var body : String
) : Serializable

data class Photo (
    var albumId : Int,
    var id : Int,
    var title : String,
    var url : String,
    var thumbnailUrl : String
) : Serializable
