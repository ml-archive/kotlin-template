package dk.nodes.data.extensions

import dk.nodes.template.core.repositories.RepositoryException
import retrofit2.Response

fun <T> Response<T>.mapException(): dk.nodes.template.core.repositories.RepositoryException {
    return dk.nodes.template.core.repositories.RepositoryException(
            this.code(),
            this.errorBody()?.string(),
            this.message()
    )
}