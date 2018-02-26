package dk.eboks.app.domain.exceptions

import dk.eboks.app.domain.models.ServerError

/**
 * Created by bison on 24-06-2017.
 */
class ServerErrorException(val error: ServerError) : RuntimeException("ServerErrorException") {

}