package dk.nodes.template.domain.interactors

/**
 * From: https://github.com/nickbutcher/plaid/blob/master/core/src/main/java/io/plaidapp/core/data/Result.kt
 */
sealed class IResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : IResult<T>()
    data class Error(val exception: Exception) : IResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}