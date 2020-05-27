package com.petertackage.songapidemo.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.petertackage.livedatatest.test
import com.petertackage.songapidemo.service.FakeArtistService
import com.petertackage.songapidemo.test.TestDispatcherRule
import com.petertackage.songapidemo.test.data.artistTestFixture
import com.petertackage.songapidemo.test.provideTestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `state is loading  on initialization when awaiting result`() =
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
    fun `state is loading, then artist list on initialization when awaiting result`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val artists = listOf(
                artistTestFixture(0),
                artistTestFixture(1),
                artistTestFixture(2)
            )
            val delayMillis = 1000L
            fakeArtistService.delayMillis = delayMillis
            fakeArtistService.nextTopArtists = artists

            // when
            viewModel = ArtistListFragmentViewModel(
                fakeArtistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )
            val observer = viewModel.state.test()
            advanceTimeBy(delayMillis)

            // then
            assertThat(observer.values).isEqualTo(
                listOf(
                    ArtistListState.IsLoading,
                    ArtistListState.Loaded(artists)
                )
            )
        }

    @Test
    fun `state is loaded artist list on initialization`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            val artists =
                listOf(
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

}
