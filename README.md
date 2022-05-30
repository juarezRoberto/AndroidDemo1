# Android Clean Architecture
Android demo app that shows a list of superheroes, each superhero has its details and also a section for favorites.

Introduction
-----------------
This app is built with the following technologies:

[Model View View Model](https://developer.android.com/topic/architecture) This guide encompasses best practices and recommended architecture for building robust, high-quality apps.
* [UI Layer](https://developer.android.com/topic/architecture/ui-layer) The role of the UI is to display the application data on the screen and also to serve as the primary point of user interaction.
* [Data Layer](https://developer.android.com/topic/architecture/data-layer) The data layer contains application data and business logic.
* [Domain Layer](https://developer.android.com/topic/architecture/domain-layer) The domain layer is an optional layer that sits between the UI layer and the data layer.

[View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way. 

[Dagger Hilt](https://developer.android.com/training/dependency-injection) Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.

[Room](https://developer.android.com/training/data-storage/room) The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.

[Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer. 

[Kotlin Flows](https://developer.android.com/kotlin/flow) In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value.

[Pagging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) The Paging library helps you load and display pages of data from a larger dataset from local storage or over network.

[View Binding](https://developer.android.com/topic/libraries/view-binding) View binding is a feature that allows you to more easily write code that interacts with views.

[Coroutines](https://developer.android.com/kotlin/coroutines) A coroutine is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.

[Coroutines Best Practices](https://developer.android.com/kotlin/coroutines/coroutines-best-practices) Best practices that have a positive impact by making your app more scalable and testable when using coroutines.

[DataStore](https://developer.android.com/topic/libraries/architecture/datastore) Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.

[Glide](https://github.com/bumptech/glide) Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface.

## Architecture
This app uses google recommended architecture.

![](https://developer.android.com/topic/libraries/architecture/images/mad-arch-overview.png)


