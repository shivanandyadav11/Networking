package online.example.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import online.example.model.User
import online.example.service.UserRepository

class UserSearchViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Loading)
    internal val searchViewState = _searchViewState.asStateFlow()


    internal fun searchQuery(query: String) = viewModelScope.launch {
        _searchViewState.value = SearchViewState.Loading
        repository.searchUsers(query).collect { result ->
            _searchViewState.value = SearchViewState.SearchResults(result)
        }
    }

    sealed class SearchViewState {
        object Loading : SearchViewState()
        data class SearchResults(val results: List<User>) : SearchViewState()
    }
}