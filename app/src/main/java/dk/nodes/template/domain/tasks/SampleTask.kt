package dk.nodes.template.domain.tasks

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.injection.factories.ChildWorkerFactory
import timber.log.Timber

class SampleTask @AssistedInject constructor(
    @Assisted params: WorkerParameters,
    @Assisted context: Context,
    private val postRepository: PostRepository
) : Worker(context, params) {
    override fun doWork(): Result {
        Timber.d("postRepository injected!")
        return Result.success()
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}