package dk.eboks.app.domain.exceptions

/**
 * Created by bison on 24-06-2017.
 */
class RepositoryException(code : Int = -1, msg : String = "Unknown") : RuntimeException(msg) {

}