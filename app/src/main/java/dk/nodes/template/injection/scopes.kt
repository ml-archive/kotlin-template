package dk.nodes.template.injection
import javax.inject.Scope

/**
 * Created by bison on 26-07-2017.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope