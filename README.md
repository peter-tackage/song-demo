# Song API Demo

- Uses ViewModels, Kotlin Coroutines and LiveData to provide data from a JSON API.
- Uses Navigation Component to navigate between the List and Detail screens.
- Uses View Binding to reference views.
- Doesn't use a dependency injection library, but ViewModel dependencies are generally provided via the constructor. 
This means that they are largely testable, although I haven't implemented tests yet.
- Has simple `MediaPlayer` for playing track audio. For simplicity, the audio is released when app is closed.
- Git history show progression of the implementation.

Third-party libraries used:
- Retrofit: for API interactions
- OkHttp: for HTTP logging
- Moshi: for JSON deserialization
- Glide: for image loading

Some inspiration drawn from my other repositories [here](https://github.com/peter-tackage/fib) and
[here](https://github.com/peter-tackage/freesound-android).

Further improvements:
- Remove duplication in the behaviour of the ViewModel's Loading, Content, Error handling and in the RecyclerView logic.
- UI is very basic - could use a few coats of paint.
- Audio player handling is rough and unproven.

