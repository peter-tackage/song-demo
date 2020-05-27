package com.petertackage.songapidemo.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.petertackage.songapidemo.service.Artist
import com.petertackage.songapidemo.service.ArtistService
import com.petertackage.songapidemo.service.Track
import com.petertackage.songapidemo.test.TestDispatcherRule
import com.petertackage.songapidemo.test.provideTestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtistListFragmentViewModelTest {

    @get:Rule
    val liveDataMainThreadRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = TestDispatcherRule()

    private lateinit var fakeArtistService: FakeArtistService

    private lateinit var viewModel: ArtistListFragmentViewModel

    @Before
    fun setUp() {
        fakeArtistService = FakeArtistService()
    }

    @Test
    fun `state is loading list on initialization when awaiting result`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val artists = listOf(
                artistTestFixture(0),
                artistTestFixture(1),
                artistTestFixture(2)
            )
            fakeArtistService.delayMillis = 1000
            fakeArtistService.nextTopArtists = artists

            // when
            viewModel = ArtistListFragmentViewModel(
                fakeArtistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )

            // then
            assertThat(viewModel.state.value).isEqualTo(ArtistListState.IsLoading)
        }

    @Test
    fun `state is loaded artist list on initialization`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val artists = listOf(
                artistTestFixture(0),
                artistTestFixture(1),
                artistTestFixture(2)
            )
            fakeArtistService.nextTopArtists = artists

            // when
            viewModel = ArtistListFragmentViewModel(
                fakeArtistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )

            // then
            assertThat(viewModel.state.value).isEqualTo(ArtistListState.Loaded(artists))
        }

    @Test
    fun `state is failed on initialization if service error`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            fakeArtistService.nextError = RuntimeException("error")

            // when
            viewModel = ArtistListFragmentViewModel(
                fakeArtistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )

            // then
            assertThat(viewModel.state.value).isInstanceOf(ArtistListState.Failed::class.java)
        }

    private fun artistTestFixture(index: Int): Artist {
        return Artist(
            "id$index",
            "nameId$index",
            "name$index",
            "avatarUrl$index",
            "backgroundUrl$index",
            "description$index",
            index,
            index.toLong() + 1
        )
    }

    class FakeArtistService : ArtistService {

        // https://youtrack.jetbrains.com/issue/KT-3110
        // You can't have public setters without public getters
        var delayMillis: Long = 0
        var nextTopArtists: List<Artist> = emptyList()
        var nextError: Throwable? = null

        override suspend fun topArtists(): List<Artist> {
            performDelay()
            throwIfError()
            return nextTopArtists
        }

        private fun throwIfError() {
            if (nextError != null) {
                throw nextError as Throwable
            }
        }

        private suspend fun performDelay() {
            if (delayMillis > 0) delay(delayMillis)
        }

        override suspend fun artist(name: String): Artist {
            TODO("Not yet implemented")
        }

        override suspend fun tracksForArtist(artistName: String): List<Track> {
            TODO("Not yet implemented")
        }

    }

}