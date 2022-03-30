package com.example.food

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.food.api.RetrofitClient
import com.example.food.models.DefaultResponse
import com.example.food.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_sign_up.buttonSignUp
import kotlinx.android.synthetic.main.activity_sign_up.editTextEmail
import kotlinx.android.synthetic.main.activity_sign_up.editTextName
import kotlinx.android.synthetic.main.activity_sign_up.editTextPassword
import kotlinx.android.synthetic.main.activity_sign_up.editTextSchool
import kotlinx.android.synthetic.main.activity_sign_up.textViewLogin
import retrofit2.Response
import javax.security.auth.callback.Callback

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textViewLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }


        buttonSignUp.setOnClickListener {

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val name = editTextName.text.toString().trim()
            val school = editTextSchool.text.toString().trim()


            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if(name.isEmpty()){
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if(school.isEmpty()){
                editTextSchool.error = "School required"
                editTextSchool.requestFocus()
                return@setOnClickListener
            }


            RetrofitClient.instance.createUser(email, name, password, school)
                .enqueue(object: retrofit2.Callback<DefaultResponse> {
                    override fun onFailure(call: retrofit2.Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: retrofit2.Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }

                })

        }
    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}