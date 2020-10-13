package dk.nodes.template.core.interfaces.repositories

data class RepositoryException(
    val code: Int,
    val errorBody: String?,
    val msg: String
) : RuntimeException(msg)