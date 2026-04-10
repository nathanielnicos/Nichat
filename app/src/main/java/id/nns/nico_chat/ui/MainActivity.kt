package id.nns.nico_chat.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.google.android.material.navigationrail.NavigationRailView
import id.nns.nico_chat.R
import id.nns.nico_chat.databinding.ActivityMainBinding
import id.nns.nico_chat.ui.sign_in.SignInActivity
import id.nns.nico_chat.utils.ConnectionChecker
import id.nns.nico_chat.utils.UserPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionChecker: ConnectionChecker
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectionChecker = ConnectionChecker(this)
        userPreference = UserPreference(this)

        if (userPreference.getUserPref().id == "") {
            goToSignInActivity()
        }

        val name = userPreference.getUserPref().name
        val profilePhoto = userPreference.getUserPref().photoUrl

        binding.tvTitle?.text = getString(R.string.title, name)
        binding.civUserPhoto?.load(
            if (profilePhoto == "") {
                R.drawable.profile
            } else {
                profilePhoto
            }
        )

        val navView: BottomNavigationView? = binding.navView
        val navRailView: NavigationRailView? = binding.navRailView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView?.setupWithNavController(navController)
        navRailView?.setupWithNavController(navController)

        observeValue()
    }

    private fun observeValue() {
        connectionChecker.observe(this) {
            if (it) {
                binding.connectionIndicator?.setImageResource(R.drawable.ic_online_indicator)
            } else {
                binding.connectionIndicator?.setImageResource(R.drawable.ic_offline_indicator)
            }
        }
    }

    private fun goToSignInActivity() {
        startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        finish()
    }
}
