package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.Sender

/**
 * Created by bison on 01/02/18.
 */
interface SendersRepository {
    fun getSenders(cached : Boolean = false) : List<Sender>
}