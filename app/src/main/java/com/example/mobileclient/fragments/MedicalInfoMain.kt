package com.example.mobileclient.fragments

import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.mobileclient.R
import com.example.mobileclient.adapter.AllergyAdapter
import com.example.mobileclient.adapter.ChronicDiseasesAdapter
import com.example.mobileclient.databinding.FragmentMedicalInfoMainBinding
import com.example.mobileclient.model.Allergy
import com.example.mobileclient.model.MedicalInfo
import com.example.mobileclient.model.User
import com.example.mobileclient.model.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalInfoMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalInfoMain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMedicalInfoMainBinding? = null
    private val sharedViewModel: UserViewModel by activityViewModels()

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedicalInfoMainBinding.inflate(inflater, container, false)
        val view = binding.root
        //Data before api request is handled
        var medicalInfo: MedicalInfo? = MedicalInfo(0, "A_PLUS", "Brak", "Brak")
        val allergiesEmpty: List<Allergy> = mutableListOf(
            Allergy(medicalInfo!!.allergies, "Jedzenie"),
        )
        val chronicDiseasesEmpty: List<String> = mutableListOf(
            medicalInfo.chronicDiseases
        )
        binding.allergyView.adapter = AllergyAdapter(allergiesEmpty)
        binding.allergyView.setHasFixedSize(true)
        binding.diseaseView.adapter = ChronicDiseasesAdapter(chronicDiseasesEmpty)
        binding.diseaseView.setHasFixedSize(true)
        //
        sharedViewModel.getMedicalInfoResponse(2)
        sharedViewModel.getUserMedicalInfoResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                medicalInfo = response.body()
                when (medicalInfo!!.bloodType) {
                    "A_PLUS" -> binding.imageView.setImageResource(R.drawable.blood_type_a_plus)
                    "A_MINUS" -> binding.imageView.setImageResource(R.drawable.blood_type_a_minus)
                    "AB_PLUS" -> binding.imageView.setImageResource(R.drawable.blood_type_ab__plus)
                    "AB_MINUS" -> binding.imageView.setImageResource(R.drawable.blood_type_ab_minus)
                    "B_PLUS" -> binding.imageView.setImageResource(R.drawable.blood_type_b_plus)
                    "B_MINUS" -> binding.imageView.setImageResource(R.drawable.blood_type_b_minus)
                    "O_PLUS" -> binding.imageView.setImageResource(R.drawable.blood_type_0_plus)
                    "O_MINUS" -> binding.imageView.setImageResource(R.drawable.blood_type_0_minus)
                    "UNKNOWN" -> binding.imageView.setImageResource(R.drawable.blood_type_add)
                    "" -> binding.imageView.setImageResource(R.drawable.blood_type_add)
                }
                val allergiesFromApi: List<Allergy> = mutableListOf(
                    Allergy(medicalInfo!!.allergies, "Jedzenie"),
                )
                val chronicDiseasesFromApi: List<String> = mutableListOf(
                    medicalInfo!!.chronicDiseases
                )
                binding.allergyView.adapter = AllergyAdapter(allergiesFromApi)
                binding.diseaseView.adapter = ChronicDiseasesAdapter(chronicDiseasesFromApi)
            } else {
                Toast.makeText(context, "Server error" + response.code(), LENGTH_LONG).show()
                Log.d("getUserMedicalInfoResponseBody", response.body().toString())
                Log.d("getUserMedicalInfoResponseCode", response.code().toString())
            }


        }
        binding.imageView.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_medicalInfoMain_to_bloodTypeForm)
        }
        binding.bandButton.setOnClickListener {
            sharedViewModel.getUserInfo(2)
            sharedViewModel.getUserInfoResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    val user: User? = response.body()
                    if (user != null) {
                        Toast.makeText(context, user.bandCode, LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error ${response.code()}", LENGTH_SHORT).show()
                }
            }
        }
        binding.addButton.setOnClickListener {
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1).setTitle("Add medical info?")
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.cancel()
                    }
                    .setItems(R.array.medicalInfoArray,
                        DialogInterface.OnClickListener { _, which ->
                            when (which) {
                                0 -> {
                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_medicalInfoMain_to_bloodTypeForm)
                                }
                                1 -> {
                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_medicalInfoMain_to_allergyForm)
                                }
                                else -> {
                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_medicalInfoMain_to_diseaseForm)
                                }
                            }
                            // The 'which' argument contains the index position
                            // of the selected item
                        }).create()
                    .show()
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicalInfoMain.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicalInfoMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}