package online.example.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                    Toast.makeText(this, it.user.email, Toast.LENGTH_SHORT).show()
                }

                is MainViewModel.Result.Error -> {
                    // Handle error
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}