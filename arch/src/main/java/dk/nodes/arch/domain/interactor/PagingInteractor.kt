package dk.nodes.arch.domain.interactor

import androidx.paging.DataSource

interface PagingInteractor<S, T> {
    fun dataSourceFactory(): DataSource.Factory<S, T>
}