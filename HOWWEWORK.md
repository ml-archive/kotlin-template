# kotlin-template
Our newest template for client projects use [Google's ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) with a lightweight ViewState approach, similar to MVI.

## Flow of control
Example: 
1. View subscribes to ViewModel's LiveData instance(s).
2. User clicks a button that loads a list of posts in a view.
3. OnClickListener executes a Interactor/UseCase asynchronously in the business logic layer.
4. The Interactor runs in the background accessing a post repository which fetches a list of posts
5. ViewModel gets result from the Interactor and updates local view state, which triggers a LiveData update
6. View is updated since it's observing the LiveData instance from our ViewModel.

## Modules
We try enforce clean architecture via modularization. Our projects are split in the following modules:

### App
Main entry point with shared Application logic

### Data
Contains data class models, repositories and network logic. Retrofit2/OkHttp3 is used for network logic.

```kotlin
class RestPostRepository @Inject constructor(private val api: Api) : PostRepository {
    @Throws(RepositoryException::class)
    override suspend fun getPosts(cached: Boolean): List<Post> {
        val response = api.getPosts().execute()
        if (response.isSuccessful) {
            return response.body()
                ?: throw(RepositoryException(
                    response.code(),
                    response.message()
                ))
        }
        throw(RepositoryException(response.code(), response.message()))
    }
}
```

### Domain
General shared business logic with interactors, extensions, managers, various utility code.

An interactor usually returns a result via a suspend method. You can model the Result class as you like:

```kotlin
sealed class Result<V> {
    sealed class Success<V> : Result<V>() {
        data class StillFetching(val data: V) : Success<V>()
        data class Cached(val data: V) : Success<V>()
        data class FreshData(val data: V) : Success<V>()
        object NoData() : Success<V>()
    }

    // Error states
}
```
Or the more simple version:
```kotlin
sealed class Result {
    data class Success(val data: SomeData) : Result()
    data class Error(val e: Exception): Result()
}
```

Interactors are the link to the outer layers of the domain layer, i.e. contacting the API or fetching/saving various state.

```kotlin
class FetchPostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<List<Post>> {

    override suspend fun invoke(): List<Post> {
        return try {
            Result.Success(
                postRepository.getPosts(true)
            )
        } catch(e: Exception) {
            Result.Error(e)
        }
    }
}
```

### Presentation
Unsurprisingly holds the UI with matching ViewModels. This is usually the module branched out from if needed.

```kotlin
// Some random ViewModel
fun loadPosts() = scope.launchOnUI {
    // Using coroutines to await interactor
    val result = asyncOnIO { fetchPostsInteractor.run() }.await()

    when(result) {
        is FetchPostsInteractor.Success -> {
            // Update our LiveData viewState
            _viewState.value = _viewState.value?.copy(
                posts = result.data
                isLoading = false
            )
        }
        else -> {
            // Update our LiveData viewState
            _viewState.value = _viewState.value?.copy(
                errorMessage = Event<String>(Translation.error.postsListFailed)
                posts = emptyList()
                isLoading = false
            )
        }
    }
}
```

Views are the consumers of the ViewModel's exposed LiveData. We want the view to be as dumb and small as possible, so only put UI code here

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    viewModel.viewState.observeNonNull(this) { state ->
        showLoading(state)
        showPosts(state)
        showErrorMessage(state)
    }
    viewModel.loadPosts()
}

private fun showPosts(state: MainActivityViewState) {
    postsTextView.text = state.posts.joinToString { it.title + System.lineSeparator() }
}

private fun showLoading(state: MainActivityViewState) {
    postsProgressBar.isVisible = state.isLoading
}

private fun showErrorMessage(state: MainActivityViewState) {
    state.errorMessage?.let {
        if (it.consumed) return@let
        Snackbar.make(
            postsTextView,
            it.consume() ?: Translation.error.unknownError,
            Snackbar.LENGTH_SHORT
        )
    }
}
```

## Injection

This project is using Dagger for injection and scoping. Dagger is an annotation based dependency injection, which computes the dependency graph at compile time and verifies that everything is correctly injected at runtime.

Dagger works by defining `@Component`s that hold the scope and lifetime of objects it creates. Each `@Component` can depend on other `@Component`s by being a `@Subcomponent`. 

### Modules

In Dagger Modules are the way to specify _how_ objects are created, where components are the once who decides the lifetime of those objects.

There are different approaches to do this, given this class:
```kotlin
class RestCityRepository @Inject constructor(
        private val api: Provider<Api>,
        private val gson: Gson
) : CityRepository {
    // ... 
}
```

1) Full declaration in module via `@Provides`

```kotlin
@Provides
fun provideCityRepository(val api: Provider<Api>, val gson: Gson): CityRepository {
    return RestCityRepository(api, gson)
}
```

2) `@Bind`s and defining dependencies at implementation site

```kotlin
@Binds
abstract fun bindCityRepository(cityRepository: RestCityRepository): CityRepository
```

By method 2 we avoid having to mirror constructor dependencies and only have to define what implementation of the `CityRepository` we want to inject where needed.


### Scoping

We define scopes via the components _or_ via scope annotations. In Careem we have two modes - component scope and @AppScope, which is similar to a singleton.

If we wanted RestCityRepository to be a singleton, all we had to do was mark it as @AppScope;
```kotlin
@AppScope
class RestCityRepository @Inject constructor(
        private val api: Provider<Api>,
        private val gson: Gson
) : CityRepository {
    // ... 
}
```

## Testing

Please see our extensive guide for unit testing: https://github.com/nodes-android/guidelines/blob/master/unittesting.md

## Live Templates
The Kotlin template comes supported with a its own set of live templates which can be found at

https://github.com/nodes-android/androidstudio-livetemplates

It should make generating the boilerplate for activities and fragments easy.

## Nodes Architecture Library
The Template uses components from our Architecture library so be sure to read up on how that is used as well

https://github.com/nodes-android/nodes-architecture-android

## Inspired from the following sources:
- [Clean Architecture by Uncle Bob](http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Some dudes android implementation](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
- [Some other dudes implementation](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way)
