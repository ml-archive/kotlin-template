package dk.nodes.arch.domain.interactor.sample

import dk.nodes.arch.domain.interactor.ResultInteractor

/**
 * Created by bison on 10/10/17.
 *
 * This is a sample of how to use the interactor base classes to implement a new interactor
 * In this case its simply adds together two numbers specified in the Input data class
 *
 * Interactors always run in the background and usually publish their results to the mainthread
 * (much like an asynctask)
 */
interface AddTwoNumbersInteractor : ResultInteractor<AddTwoNumbersInteractor.Input, Int> {
    /*
        This contain whatever inputs the interactor needs to complete its job, it is set before a call to run()
        by the client (a presenter most likely)
     */
    data class Input(
        val firstNumber: Int = 10,
        val secondNumber: Int = 10
    )
}