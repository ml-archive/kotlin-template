package dk.nodes.template.domain.interactors

/**
 * From: https://github.com/nickbutcher/plaid/blob/master/core/src/main/java/io/plaidapp/core/data/Result.kt
 */
sealed class InteractorResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : InteractorResult<T>()
    data class Error(val exception: Exception) : InteractorResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}