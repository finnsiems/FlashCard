package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAddBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            var question = binding.edittextQuestion.text.toString()
            var answer = binding.edittextAnswer.text.toString()
            Log.d("NoteThing", ("Question: " + question + " Answer: " + answer))
            save(question, answer)
            findNavController().navigate(R.id.action_Review_to_FirstFragment)
        }

    }

    fun save(question: String, answer: String) {
        var counter = 0
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val editor = sharedPref.edit()
        try {
            counter = sharedPref.getInt("counter", 0)
        } catch (e: Exception) {
            Log.d("NoteThing", ("No Counter in Save:" + e))
        }
        counter++
        editor.putInt("counter", counter)
        editor.putString("question" + counter, question)
        editor.putString("answer" + counter, answer)

        //editor.putInt("counter", 0)
        editor.apply()
        Log.d("NoteThing", ("Question: " + question + " Answer: " + answer))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}