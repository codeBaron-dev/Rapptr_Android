package com.rapptrlabs.androidtest.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rapptrlabs.androidtest.Prefs
import com.rapptrlabs.androidtest.R
import com.rapptrlabs.androidtest.convertMillisecondsToTime
import com.rapptrlabs.androidtest.databinding.ActivityLoginBinding
import com.rapptrlabs.androidtest.login.model.LoginResponse
import com.rapptrlabs.androidtest.remote.responsemanager.ResponseStateHandler
import com.rapptrlabs.androidtest.repository.SharedViewModel
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val sharedViewModel: SharedViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.activity_login_title)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Restore the saved data if available
        if (savedInstanceState != null) {
            sharedViewModel.email_text = savedInstanceState.getString("email")
            sharedViewModel.password_text = savedInstanceState.getString("password")
        }

        initView()
        clickEvents()
    }

    private fun initView() {
        binding.emailEdt.setText(sharedViewModel.email_text)
        binding.passwordEdt.setText(sharedViewModel.password_text)
    }

    private fun clickEvents() {
        binding.loginBtn.setOnClickListener {
            loginUser(binding.emailEdt.text.toString(), binding.passwordEdt.text.toString())
        }
    }

    private fun loginUser(email: String, password: String) {
        sharedViewModel.login(email, password, this).observe(this) {
            when (it) {
                is ResponseStateHandler.Loading -> {/*Show loading*/
                }

                is ResponseStateHandler.Success -> {
                    displayDialog(it.data)
                }

                is ResponseStateHandler.Error -> {}
                is ResponseStateHandler.Exception -> {}
                is ResponseStateHandler.ThrowableError -> {}
            }
        }
    }

    @SuppressLint("NewApi")
    private fun displayDialog(data: LoginResponse?) {
        val requestDuration = Prefs(this).getLongValue(Prefs.REQUEST_DURATION)
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Login Response")
            .setMessage(
                "Code: ${data?.code}\nMessage: ${data?.message}\nDuration: ${
                    convertMillisecondsToTime(
                        requestDuration
                    )
                }"
            )
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onBackPressed()
            }
            .create()

        alertDialog.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedViewModel.email_text = binding.emailEdt.text.toString()
        sharedViewModel.password_text = binding.passwordEdt.text.toString()
        outState.putString("email", sharedViewModel.email_text)
        outState.putString("password", sharedViewModel.password_text)
    }
}