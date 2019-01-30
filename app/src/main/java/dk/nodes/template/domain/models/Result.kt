package dk.nodes.template.domain.models

/**
 * From: https://github.com/nickbutcher/plaid/blob/master/core/src/main/java/io/plaidapp/core/data/Result.kt
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}