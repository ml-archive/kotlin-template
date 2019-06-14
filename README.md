# kotlin-template
Our newest template for client projects use [Google's ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel) with a lightweight ViewState approach, similar to MVI.

We believe this is a much needed "standardization" of the current Android development practices that will help onboarding of new developers, but also handover to clients and general collaboration.

## Modules
We try enforce clean architecture via modularization. Our projects are split in the following modules:

### App
Main entry point with shared Application logic

### Data
Contains models, repositories and network logic.

### Domain
General shared business logic with interactors, extensions, managers, various utility code.

### Presentation
Unsurprisingly holds the UI with matching view models. This is usually the module branched out from if needed.

## Live Templates
The Kotlin template comes supported with a its own set of live templates which can be found at

https://github.com/nodes-android/androidstudio-livetemplates

It should make generating the boilerplate for activities and fragments easy.

## Nodes Architecture Library
The Template uses components from our Architecture library so be sure to read up on how that is used as well

https://github.com/nodes-android/nodes-architecture-android



__Below information might be slightly out of date__
## Layers
This is a 4 layer onion architecture. Dependencies are only allowed to point inwards, 
meaning that the inner layer most not reference code in the outer layers directly. 
From inside out it consists of:

### Entities
Models/POJOs implemented as data objects in kotlin.

### Business Logic / Use Cases
Consist of interactors and repositories (the interfaces). The interactors encapsulates the business logic
 and perform operations on the entities. Interactors are scheduled to run in the
 background and return information to the outer layer through callbacks implemented in the outer layers.
 
### Interface Adapters
ViewModel (as part of the MVVM pattern) are implemented in this layer. ViewModel holds information
from the inner layers (business logic and entities) to the user interface etc. In other words they adapt
the data for output to the outermost layer (Framework and Drivers)

### Frameworks and Drivers
This is the outmost layer consisting of things such as the User interface (for android Activities, fragments etc), database libraries, retrofit,
okhttp etc. This also contain specific implementations of the Repository interfaces the business logic layer needs to access data.

## Flow of control
Example: 
1. View subscribes to ViewModel's LiveData instance(s).
2. User clicks a button that loads a list of posts in a view.
3. OnClickListener executes a Interactor/UseCase asynchronously in the business logic layer.
4. The Interactor runs in the background accessing a post repository which fetches a list of posts
5. ViewModel gets result from the Interactor and updates local view state, which triggers a LiveData update
6. View is updated since it's observing the LiveData instance from our ViewModel.

## Patterns in use:
- MVVM
- Repository
- Interactor (implemented with a pluggable executor)
- Dependency Injection (and thus factory)
- Inward dependency rule (all dependencies must point inwards)

## Stuff
- Coroutines and LiveData as callback/async/threading handlers
- kotlin data classes as entities
- Retrofit2/OkHttp3
- Android kotlin extensions (views are automatically made into properties on the activity)
- Uses nstack-kotlin
- Mockito and junit for testing

## Inspired from the following sources:
- [Clean Architecture by Uncle Bob](http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Some dudes android implementation](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
- [Some other dudes implementation](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way)
