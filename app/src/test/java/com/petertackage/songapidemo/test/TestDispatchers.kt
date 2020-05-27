package com.petertackage.songapidemo.test

import com.petertackage.songapidemo.async.CoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
fun provideTestCoroutineDispatcherProvider(testDispatcher: TestCoroutineDispatcher): CoroutineDispatcherProvider {
    return CoroutineDispatcherProvider(
        main = testDispatcher,
        io = testDispatcher,
        computation = testDispatcher
    )
}