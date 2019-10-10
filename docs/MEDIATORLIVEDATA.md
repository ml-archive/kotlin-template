# MediatorLiveData
`BaseViewModel` generally provides and manages single ViewState. ViewState is wrapped into  `MediatorLiveData` so it is possible to combine multiple `LiveData` sources using this method:
```kotlin
protected fun <T> addStateSource(source: LiveData<T>, onChanged: (T) -> Unit) {
       _viewState.addSource(source, onChanged)
   }
```

## Combining Source from Interactor
Presentation layer provides various extension functions for your interactors so you could use different approaches when updating your view state, one of them is `LiveDataInteractor`. `LiveDataInteactor` will produce a `LiveData<InteractorResult<T>>` that can be used as an additional state source.

```kotlin
// Wrap your basic interactor as LiveData
private val liveDataInteractor = postsInteractor.asLiveData()

   fun fetchPosts() = viewModelScope.launch(Dispatchers.Main) {
       addStateSource(resultInteractor.liveData) { state = mapResult(it) }
       withContext(Dispatchers.IO)  {
           resultInteractor()
       }
   }
```

## Combining Source from Repository
Sometimes your repository exposes `LiveData`:
```kotlin
interface PostsRepository {
    fun getPosts() : LiveData<List<Posts>>
}
```
This repository implementation then can be used directly from the `viewModel` by combining it with ViewModel's viewState
```kotlin
private val liveDataRepository = LiveDataRepository()
   init {
       addStateSource(liveDataRepository.postsLiveData()) {
           state = state.copy(posts = it)
       }
   }
```
