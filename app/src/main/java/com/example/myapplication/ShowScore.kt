package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddBinding
import com.example.myapplication.databinding.ShowscoreLayoutBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ShowScore: Fragment() {

    private var _binding: ShowscoreLayoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("NoteThing", "We are here2" )

        _binding = ShowscoreLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("NoteThing", "We are here" )

        try {
            Log.d("NoteThing", "????")
            setFragmentResultListener("Scores") { requestKey, bundle ->
                val right = bundle.getInt("right")
                val wrong = bundle.getInt("wrong")
                val skip = bundle.getInt("skip")
                val time = bundle.getString("time")

                Log.d("NoteThing", "Right: " + right + " Wrong: " + wrong + " skip: " + skip + " time: " + time)

                binding.textviewRightValue.text = right.toString()
                binding.textviewWrongValue.text = wrong.toString()
                binding.textviewSkipValue.text = skip.toString()
                binding.textviewTimeValue.text = time

                setFragmentResult("requestKey", bundleOf("bundleKey" to 1))
            }
        }catch (e:Exception){
            Log.d("NoteThing", "Exception2: " + e)
        }


    }

    fun save() {
        var counter = 0
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val editor = sharedPref.edit()
        //editor.putInt("counter", 0)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}