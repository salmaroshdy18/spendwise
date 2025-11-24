package com.example.spendwise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spendwise.R
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController

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

        val btnManageCategories: MaterialButton =
            view.findViewById(R.id.btnManageCategories)

        btnManageCategories.setOnClickListener {
            // Navigate to ManageCategoriesFragment
            findNavController().navigate(
                R.id.action_settingsFragment_to_manageCategoriesFragment
            )
        }
    }
}
