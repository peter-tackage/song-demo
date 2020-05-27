package com.petertackage.songapidemo.feature.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.petertackage.livedatatest.test
import com.petertackage.songapidemo.service.Artist
import com.petertackage.songapidemo.service.ArtistService
import com.petertackage.songapidemo.test.TestDispatcherRule
import com.petertackage.songapidemo.test.provideTestCoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class ArtistListFragmentViewModelTest {

    @get:Rule
    val liveDataMainThreadRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = TestDispatcherRule()

    @Mock
    private lateinit var artistService: ArtistService

    private lateinit var viewModel: ArtistListFragmentViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
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
            `when`(artistService.topArtists()).thenReturn(artists)

            // when
            viewModel = ArtistListFragmentViewModel(
                artistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )
            val observer = viewModel.state.test()

            // then
            assertThat(observer.value).isEqualTo(ArtistListState.Loaded(artists))
        }

    @Test
    fun `state is failed on initialization if service error`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // given
            `when`(artistService.topArtists()).thenThrow(RuntimeException("error"))

            // when
            viewModel = ArtistListFragmentViewModel(
                artistService,
                provideTestCoroutineDispatcherProvider(coroutinesTestRule.testDispatcher)
            )
            val observer = viewModel.state.test()

            // then
            assertThat(observer.value).isInstanceOf(ArtistListState.Failed::class.java)
        }

    private fun artistTestFixture(index: Int): Artist {
        return Artist(
            "id$index",
            "nameId$index",
            "name$index",
            "avatarUrl$index",
            "backgroundUrl$index",
            "descriptiob$index",
            index,
            index.toLong() + 1
        )
    }
}