package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentReviewBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    var mode = 0
    var right = "Right: "
    var wrong = "Wrong: "
    var skipped = "Skipped: "
    var rightCounter = 0
    var wrongCounter = 0
    var skippedCounter = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var answer = ""
    var questions = arrayListOf<String>()
    var answers = arrayListOf<String>()
    var positionCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rightCounter = 0
        wrongCounter = 0
        skippedCounter = 0
        positionCounter = 0

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getInt("bundleKey")
            Log.d("NoteThing", "Result:" + result)
            mode = result
            Log.d("NoteThing", "Mode:" + mode)

            if(mode == 1) {
                try {
                    binding.textviewRight.setText(right)
                    binding.textviewCounterRight.setText(rightCounter.toString())

                    binding.textviewWrong.setText(wrong)
                    binding.textviewCounterWrong.setText(wrongCounter.toString())

                    binding.textviewSkip.setText(skipped)
                    binding.textviewCounterSkip.setText(skippedCounter.toString())

                    val timer = binding.simpleChronometer
                    timer.visibility = View.VISIBLE
                    timer.start()
                }catch (e:Exception){
                    Log.d("NoteThing", "Error: " + e)
                }
            }

        }

        load()

        binding.buttonDelete.setOnClickListener {
            questions.removeAt(positionCounter)
            answers.removeAt(positionCounter)
            save()
            questions.clear()
            answers.clear()
            load()
        }

        binding.buttonSkip.setOnClickListener {
           skip()
        }

        binding.buttonCheck.setOnClickListener {
           check()
        }

        binding.buttonShowanswer.setOnClickListener {
            binding.edittextType.setText(answers[positionCounter])
        }
    }

    fun skip(){
        try{
            positionCounter++
            skippedCounter++
            if(positionCounter >= questions.size) {
                if(mode == 1){
                    showScores()
                } else positionCounter = 0
            }
            binding.textviewSecond.setText(questions[positionCounter])

            if(mode == 1) binding.textviewCounterSkip.setText(skippedCounter.toString())
        }catch (e:Exception){
            Log.d("NoteThing", "welp: " + e)
        }
    }

    fun check(){
        try{
            val textInput = binding.edittextType.text.toString()
            Log.d("NoteThing", "A:" + answer)

            if(textInput == ""){
                Toast.makeText(this.context, "Please type an answer", Toast.LENGTH_LONG).show()
            }
            else {
                if (textInput == answers[positionCounter] ){
                    Toast.makeText(this.context, "Correct!", Toast.LENGTH_LONG).show()

                    if(mode == 1){
                        rightCounter++
                        binding.textviewCounterRight.setText(rightCounter.toString())
                    }

                    positionCounter++
                    binding.textviewSecond.setText(questions[positionCounter])
                    binding.edittextType.setText("")

                }else{
                    Toast.makeText(this.context, "Wrong!", Toast.LENGTH_LONG).show()
                    if(mode == 1){
                        wrongCounter++
                        binding.textviewCounterWrong.setText(wrongCounter.toString())
                        positionCounter++
                        binding.textviewSecond.setText(questions[positionCounter])
                        binding.edittextType.setText("")
                    }
                }

                if(positionCounter >= questions.size) {
                    if(mode == 1){
                        showScores()
                    }
                    else positionCounter = 0
                }
            }
        }catch (e:Exception){
            Log.d("NoteThing","ahh: " + e)
        }
    }


    fun load(){
        val counter: Int
        var question: String
        var countVar = 1
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        questions.clear()

        try {
            counter = sharedPref.getInt("counter", 0)
            while(countVar<=counter) {
                question = sharedPref.getString(("question" + countVar), "").toString()
                questions.add(question)
                answer = sharedPref.getString(("answer" + countVar), "").toString()
                answers.add(answer)
                Log.d("NoteThing", "Q: " + question + " A: " + answer + " CountVar: " + countVar)
                countVar++
            }

            binding.textviewSecond.setText(questions[0])
        }catch (e:Exception) {
            Log.d("NoteThing", ("No Counter:" + e))
        }
    }

    fun save() {
        var counter = 0
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val editor = sharedPref.edit()

        for(question in questions){
            counter++
            editor.putString("question" + counter, question)
            Log.d("NoteThing", ("Question: " + question + " Counter: " + counter))
        }
        counter = 0
        for(answer in answers){
            counter++
            editor.putString("answer" + counter, answer)
            Log.d("NoteThing", ("Answer" + answer + " Counter: " + counter))
        }

        editor.putInt("counter", counter)
        //editor.putInt("counter", 0)
        editor.apply()
    }

    fun showScores(){
        try {
            val timeValue = binding.simpleChronometer.text.toString()
            Log.d("NoteThing", "TimeValue:" + timeValue)
            setFragmentResult(
                "Scores",
                bundleOf(
                    "right" to rightCounter,
                    "wrong" to wrongCounter,
                    "skip" to skippedCounter,
                    "time" to timeValue
                )
            )
            Log.d("NoteThing", "Time: " + timeValue )
            findNavController().navigate(R.id.action_Review_to_Scores)
            Log.d("NoteThing", "Hello?")
        }catch (e:Exception){
            Log.d("NoteThing", "Exception: " + e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}