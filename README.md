# kotlin-template
Kotlin Clean Architecture Template Project
A stab at clean arch in android using kotlin.

### Layers
This is a 4 layer onion architecture. Dependencies are only allowed to point inwards, 
meaning that the inner layer most not reference code in the outer layers directly. 
From inside out it consists of:

#### Entities
Models/POJOs implemented as data objects in kotlin.

#### Business Logic / Use Cases
Consist of interactors and repositories (the interfaces). The interactors encapsulates the business logic
 and perform operations on the entities. Interactors are scheduled to run in the
 background and return information to the outer layer through callbacks implemented in the outer layers.
 
#### Interface Adapters
Presenters (as part of the MVP pattern) are implemented in this layer. Presenters present information
from the inner layers (business logic and entities) to the user interface etc. In other words they adapt
the data for output to the outermost layer (Framework and Drivers)

#### Frameworks and Drivers
This is the outmost layer consisting of things such as the User interface (for android Activities, fragments etc), database libraries, retrofit,
okhttp etc. This also contain specific implementations of the Repository interfaces the business logic layer needs to access data.

## Flow of control
Example: 
1. User clicks a button that loads a list of posts in a view.
3. OnClickListener executes a Interactor/UseCase asynchronously in the business logic layer.
4. The Interactor runs in the background accessing a post repository which fetches a list of posts
5. Presenter gets notified with the loaded posts or a message in case of error trough a callback
6. Presenter instructs the view (if attached) to update itself with the new data, or display an error to the user

## Patterns in use:
- MVP
- Repository
- Interactor (implemented with kotlin coroutines)

## Stuff
- kotlin data classes as entities
- Retrofit2/OkHttp3
- Android kotlin extensions (views are automatically made into properties on the activity)
- Uses KStack
- Basic kotlin plumbing (coroutines, reflection)
- Mockito and junit for testing

## Inspired/partially ripped off from the following sources:
- [Clean Architecture by Uncle Bob](http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Some dudes android implementation](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
- [Some other dudes implementation](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way)
