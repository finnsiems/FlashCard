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
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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

        binding.buttonDelete.setOnClickListener {
            questions.removeAt(positionCounter)
            answers.removeAt(positionCounter)
            save()
            questions.clear()
            answers.clear()
            load()
        }

        binding.buttonSkip.setOnClickListener {
            positionCounter++
            if(positionCounter > questions.size-1) {
                if(mode == 1) resultPopup()
                else positionCounter = 0
            }
            binding.textviewSecond.setText(questions[positionCounter])

            skippedCounter++

            if(mode == 1) binding.textviewCounterSkip.setText(skippedCounter.toString())

        }

        load()

        binding.buttonCheck.setOnClickListener {
           check()
        }

        binding.buttonShowanswer.setOnClickListener {
            binding.edittextType.setText(answers[positionCounter])
        }
    }

    fun check(){
        val textInput = binding.edittextType.text.toString()
        Log.d("NoteThing", "A:" + answer)
        if (textInput == answers[positionCounter] ){
            Toast.makeText(this.context, "Correct!", Toast.LENGTH_LONG).show()


            if(mode == 1){
                rightCounter++
                binding.textviewCounterRight.setText(rightCounter.toString())
            }

            positionCounter++
            if(positionCounter > questions.size-1) {
                if(mode == 1) resultPopup()
                else positionCounter = 0
            }
            binding.textviewSecond.setText(questions[positionCounter])
            binding.edittextType.setText("")

        }else if(textInput == ""){
            Toast.makeText(this.context, "Please type an answer", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this.context, "Wrong!", Toast.LENGTH_LONG).show()

            if(mode == 1){
                wrongCounter++
                binding.textviewCounterWrong.setText(wrongCounter.toString())
            }
        }
    }


    fun load(){
        val counter: Int
        var question: String
        var countVar = 1
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

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

    fun resultPopup(){

        // inflate the layout of the popup window
        /*val inflater = getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup, null)


        // create the popup window

        // create the popup window
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val focusable = true
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        */


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}