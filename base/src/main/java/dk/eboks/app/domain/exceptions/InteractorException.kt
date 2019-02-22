package dk.eboks.app.domain.exceptions

/**
 * Created by bison on 24-06-2017.
 */
class InteractorException(msg: String = "Unknown interactor exception", val code: Int = -1) :
    RuntimeException(msg)