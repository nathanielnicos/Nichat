package id.nns.nico_chat.ui.sign_in

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.nns.nico_chat.R
import id.nns.nico_chat.data.response.google_sheet.UserResponse
import id.nns.nico_chat.databinding.ActivitySignInBinding
import id.nns.nico_chat.ui.MainActivity
import id.nns.nico_chat.utils.UserPreference

class SignInActivity : AppCompatActivity() {
    private val signInViewModel: SignInViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaultState()

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        userPreference = UserPreference(this)

        onEvent()
        observeValue()
    }

    private fun onEvent() {
        binding.rgEntryChoice.setOnCheckedChangeListener { _, checkedId ->
            changeScreenState(checkedId)
        }

        binding.btnGoogleSignIn.setOnClickListener {
            it.isEnabled = false
            signInWithGoogle()
        }

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.btnVerify.setOnClickListener {
            verifyAccount()
        }
    }

    private fun observeValue() {
        signInViewModel.userResponse.observe(this) {
            if (it != null) {
                userPreference.setUserPref(it)
                updateUI()
            }
        }
        signInViewModel.postResponse.observe(this) {
            if (!it.isSuccess) {
                binding.btnGoogleSignIn.isEnabled = true
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDefaultState() {
        binding.rbSignIn.isChecked = true
        changeScreenState(binding.rbSignIn.id)
    }

    private fun changeScreenState(checkedId: Int) {
        if (checkedId == binding.rbSignIn.id) {
            binding.apply {
                tvUserName.visibility = View.GONE
                etUserName.visibility = View.GONE
                tvUserConfirmPassword.visibility = View.GONE
                etUserConfirmPassword.visibility = View.GONE
                btnSignUp.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                tvUserName.visibility = View.VISIBLE
                etUserName.visibility = View.VISIBLE
                tvUserConfirmPassword.visibility = View.VISIBLE
                etUserConfirmPassword.visibility = View.VISIBLE
                btnSignUp.visibility = View.VISIBLE
                btnSignIn.visibility = View.GONE
            }
        }
    }

    private fun signIn() {
        val email = binding.etUserEmail
        val password = binding.etUserPassword

        when {
            email.text.isNullOrBlank() -> {
                email.error = "Email cannot be empty!"
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.text).matches() -> {
                email.error = "Invalid email format!"
            }
            password.text.isNullOrBlank() -> {
                password.error = "Password cannot be empty!"
            }
            password.text.length < 8 -> {
                password.error = "Password must be at least 8 characters!"
            }
            else -> {
//                mainViewModel.signIn(
//                    email = email.text.toString(),
//                    password = password.text.toString()
//                )
            }
        }
    }

    private fun signUp() {
        val name = binding.etUserName
        val email = binding.etUserEmail
        val password = binding.etUserPassword
        val confirmPassword = binding.etUserConfirmPassword

        when {
            name.text.isNullOrBlank() -> {
                name.error = "Name cannot be empty!"
            }
            email.text.isNullOrBlank() -> {
                email.error = "Email cannot be empty!"
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.text).matches() -> {
                email.error = "Invalid email format!"
            }
            password.text.isNullOrBlank() -> {
                password.error = "Password cannot be empty!"
            }
            password.text.length < 8 -> {
                password.error = "Password must be at least 8 characters!"
            }
            confirmPassword.text.isNullOrBlank() -> {
                confirmPassword.error = "Confirm password cannot be empty!"
            }
            confirmPassword.text.toString() != password.text.toString() -> {
                confirmPassword.error = "Confirm password must be same!"
            }
            else -> {
//                mainViewModel.signUp(
//                    name = name.text.toString(),
//                    email = email.text.toString(),
//                    password = password.text.toString()
//                )
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toast.makeText(
                    this,
                    getString(R.string.sign_in_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        getString(R.string.sign_in_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    firebaseUser = auth.currentUser as FirebaseUser

                    val user = UserResponse(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName.toString(),
                        email = firebaseUser.email.toString(),
                        photoUrl = firebaseUser.photoUrl.toString(),
                        isActive = true
                    )

                    signInViewModel.checkUserSheet(user)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.sign_in_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun showVerificationScreen() {
        binding.clEntryScreen.visibility = View.GONE
        binding.clVerificationCode.visibility = View.VISIBLE
    }

    private fun verifyAccount() {
//        val id = userResponse.id
//
//        if (binding.etVerificationCode.text.toString() == id) {
//            mainViewModel.verifyAccount(id)
//        }
    }

    private fun updateUI() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (userPreference.getUserPref().id != "") {
            updateUI()
        }
    }
}
