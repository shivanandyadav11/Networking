package online.example.networking

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import online.example.model.Address
import online.example.model.Geo
import online.example.model.User
import online.example.service.UserRepository
import online.example.viewModel.UserSearchViewModel
import online.example.viewModel.UserSearchViewModel.SearchViewState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class UserSearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserSearchViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = UserSearchViewModel(repository)
    }

    @Test
    fun `searchQuery should maintain Loading state during sequential searches`() = runTest {
        // Given
        val users1 = listOf(
            User(
                id = 1,
                name = "Jake Harper",
                username = "jake11",
                email = "jakeharper@gmail.com",
                address = Address(
                    street = "123 Main St",
                    suite = "Apt 101",
                    city = "Springfield",
                    zipcode = "12345",
                    geo = Geo(
                        lat = "0",
                        lng = "0"
                    )
                )
            )
        )
        val users2 = listOf(
            User(
                id = 1,
                name = "Charlie Harper",
                username = "charlie11",
                email = "charlieharper@gmail.com",
                address = Address(
                    street = "123 Main St",
                    suite = "Apt 101",
                    city = "Springfield",
                    zipcode = "12345",
                    geo = Geo(
                        lat = "0",
                        lng = "0"
                    )
                )
            )
        )

        coEvery { repository.searchUsers("Jake") } returns flowOf(users1)
        coEvery { repository.searchUsers("Charlie") } returns flowOf(users2)

        // When & Then
        viewModel.searchViewState.test {
            // Initial state should be Loading
            assertEquals(SearchViewState.Loading, awaitItem())

            // First search
            viewModel.searchQuery("Jake")
            assertEquals(SearchViewState.SearchResults(users1), awaitItem())

            // Second search
            viewModel.searchQuery("Charlie")
            assertEquals(SearchViewState.Loading, awaitItem())
            assertEquals(SearchViewState.SearchResults(users2), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}