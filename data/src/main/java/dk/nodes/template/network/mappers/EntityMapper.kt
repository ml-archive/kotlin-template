package dk.nodes.template.network.mappers

interface EntityMapper<T, R> {
    fun mapEntity(t: T): R
}