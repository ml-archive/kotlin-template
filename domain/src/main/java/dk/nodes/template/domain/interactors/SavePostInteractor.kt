package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.interactors.base.BaseInputInteractor
import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class SavePostInteractor @Inject constructor(
        private val postRepository: PostRepository
) : BaseInputInteractor<Post, Unit> {

    override suspend fun invoke(input: Post): InteractorResult<Unit> {
        return try {
            postRepository.savePost(input)
            success
        } catch (e: RepositoryException) {
            error(e)
        }
    }
}