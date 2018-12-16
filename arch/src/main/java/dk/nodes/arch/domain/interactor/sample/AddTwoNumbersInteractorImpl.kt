package dk.nodes.arch.domain.interactor.sample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by bison on 10/10/17.
 */
class AddTwoNumbersInteractorImpl() : AddTwoNumbersInteractor {
    override val dispatcher: CoroutineDispatcher = Dispatchers.Default

    override suspend fun invoke(executeParams: AddTwoNumbersInteractor.Input) {
        output?.onAddTwoNumbersResult(executeParams.firstNumber + executeParams.secondNumber)
    }

    private var output: AddTwoNumbersInteractor.Output? = null

    override fun setOutput(output: AddTwoNumbersInteractor.Output) {
        this.output = output
    }
}

