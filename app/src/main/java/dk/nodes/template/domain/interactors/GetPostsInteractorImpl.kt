package dk.nodes.template.domain.interactors

import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException

class GetPostsInteractorImpl(
    executor: Executor,
    private val postRepository: PostRepository
) : BaseInteractor(executor), GetPostsInteractor {
    override var output: GetPostsInteractor.Output? = null
    override var input: GetPostsInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input
        }
        try {
            val posts = postRepository.getPosts(true)
            runOnUIThread {
                output?.onPostsLoaded(posts)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onError(e.message ?: "Unknown error")
            }
        }
    }
}