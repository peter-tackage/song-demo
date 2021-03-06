# Song API Demo

- Uses ViewModels, Kotlin Coroutines and LiveData to provide data from a JSON API.
- Uses Navigation Component to navigate between the List and Detail screens.
- Uses View Binding to reference views.
- Doesn't use a dependency injection library, but ViewModel dependencies are generally provided via the constructor. 
This means that they are largely testable, although I haven't implemented tests yet. Using a dependency injection library, such as Dagger seemed like overkill.
- Has simple `MediaPlayer` for playing track audio. For simplicity, the audio is released when app is closed. 
Chose this rather than `ExoPlayer` for simplicity, although I could borrowed from myself; [here](https://github.com/peter-tackage/freesound-android/tree/master/app/src/main/java/com/futurice/freesound/feature/audio)
- Git history shows the progression of the implementation.

Third-party libraries used:
- Retrofit: for API interactions
- OkHttp: for HTTP logging
- Moshi: for JSON deserialization
- Glide: for image loading

Some inspiration drawn from my other repositories [here](https://github.com/peter-tackage/fib) and
[here](https://github.com/peter-tackage/freesound-android).

Further improvements:
- Remove duplication in the behaviour of the ViewModel's Loading, Content, Error handling 
- Remove duplication of RecyclerView logic.
- Remove duplication of `emit` methods in ViewModels.
- UI is very basic - could use a few coats of paint.
- Audio player handling is rough and unproven.
- Various other TODOs/FIXMEs where I made to the trade-off to move forward, rather than polish.

Code Coverage:

Jacoco is defined for the debug variants:

Run `./gradlew test jacocoDebug`

Output is in `./app/build/jacoco`
