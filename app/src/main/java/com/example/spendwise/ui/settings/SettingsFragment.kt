package com.example.spendwise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spendwise.R
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import android.content.Intent
import android.content.Context
import com.example.spendwise.ui.WelcomeActivity



class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnManageCategories: MaterialButton = view.findViewById(R.id.btnManageCategories)
        val btnAbout: MaterialButton = view.findViewById(R.id.btnAbout)
        val btnGoals: MaterialButton = view.findViewById(R.id.btnGoals)   // NEW

        btnManageCategories.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_manageCategoriesFragment
            )
        }

        btnAbout.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_aboutFragment
            )
        }

        btnGoals.setOnClickListener {
            findNavController().navigate(
                R.id.action_settingsFragment_to_goalsFragment   // NEW
            )
        }

        // ----- Dark mode switch -----
        val switchDark: SwitchMaterial = view.findViewById(R.id.switchDarkMode)

// Use separate prefs for settings
        val settingsPrefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDark = settingsPrefs.getBoolean("dark_mode", false)

// Apply saved mode
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        switchDark.isChecked = isDark

// Toggle listener
        switchDark.setOnCheckedChangeListener { _, checked ->
            settingsPrefs.edit().putBoolean("dark_mode", checked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

// ----- Logout button -----
        val btnLogout: MaterialButton = view.findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Clear saved user session (if you stored anything)
            val userPrefs = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE)
            userPrefs.edit().clear().apply()

            // Navigate to Welcome screen and clear backstack
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}
