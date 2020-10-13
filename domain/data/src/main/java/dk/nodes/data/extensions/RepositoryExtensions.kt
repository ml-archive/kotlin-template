package dk.nodes.data.extensions

import dk.nodes.template.core.interfaces.repositories.RepositoryException
import retrofit2.Response

fun <T> Response<T>.mapException(): RepositoryException {
    return RepositoryException(
        this.code(),
        this.errorBody()?.string(),
        this.message()
    )
}