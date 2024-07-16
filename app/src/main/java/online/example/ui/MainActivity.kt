package online.example.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import online.example.networking.R
import online.example.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { MainViewModel() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.makeApiCall()
        observeApiResult()
    }

    private fun observeApiResult() {
        viewModel.apiResult.observe(this) {
            when (it) {
                is MainViewModel.Result.Success -> {
                    // Handle success
                    it.users.forEach { user ->
                        Log.d("UserListFromLiveData", "onCreate: ${user.email}")
                        Toast.makeText(
                            this@MainActivity,
                            user.email,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is MainViewModel.Result.Error -> {
                    // Handle error
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                MainViewModel.Result.NoState -> {}
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.userResult.collect {
                    when (it) {
                        is MainViewModel.Result.Success -> {
                            it.users.forEach { user ->
                                Log.d("UserListFromFlow", "onCreate: ${user.email}")
                                Toast.makeText(
                                    this@MainActivity,
                                    user.email,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is MainViewModel.Result.Error -> {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }

                        MainViewModel.Result.NoState -> {}
                    }

                }
            }
        }
    }
}