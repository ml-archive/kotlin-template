package dk.nodes.arch.domain.interactor.sample

import dk.nodes.arch.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by bison on 10/10/17.
 */
class AddTwoNumbersInteractorImpl(
    dispatchers: AppCoroutineDispatchers
) : AddTwoNumbersInteractor {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun invoke(
        executeParams: AddTwoNumbersInteractor.Input,
        onResult: (Result<Int>) -> Unit
    ) {
        onResult.invoke(Result.Success(executeParams.firstNumber + executeParams.secondNumber))
    }
}