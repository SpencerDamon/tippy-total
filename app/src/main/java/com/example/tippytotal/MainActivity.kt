package com.example.tippytotal

import android.animation.ArgbEvaluator
import java.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial


//private const val TAG = "MainActivity"
// 11. Show a percentage when the activity first starts
private const val INITIAL_TIP_PERCENT = 15
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    // 1. Create member variables, late initialization in onCreate not in the Main Constructor
    // 8. Add tag for log, and run, open logcat, type MainActivity in search bar, and scrub
    // the seeBar you should see the value go from 1 - 30 in logcat

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var swRoundUpTip: SwitchMaterial
    private lateinit var swRoundUpTotal: SwitchMaterial
    private lateinit var etNumberInParty: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //2. pull out references to the member variables in onCreate
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        //18. Add reference for tvTipDescription
        tvTipDescription = findViewById(R.id.tvTipDescription)
        //  Add reference for RoundUp switches
        swRoundUpTip = findViewById(R.id.swRoundUpTip)
        swRoundUpTotal = findViewById(R.id.swRoundUpTotal)
        // Add reference for Number in Party edit tezt view
        etNumberInParty = findViewById(R.id.etNumberInParty)




        //12. Assign constant value to seekBar indicator, placing it in the center of its width
        // when activity first starts,
        seekBarTip.progress = INITIAL_TIP_PERCENT

        //13. Assign constant value to show a percentage when activity first starts
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"

        // 21. Call updateDescription so it shows the value before user inputs amount
        updateTipDescription(INITIAL_TIP_PERCENT)

        //3. We get notified by the seek bar if we add a listener to it
        // tell the android system what to do, when something has happened,
        // by defining  this class inside the parentheses
        // object will get a red squiggly, click on light bulb, click implement members
        // to override 3 methods that must now be changed to comply with the definition of the
        // onSeekBarChangeListener.
        //We are defining an anonymous class which implements this interface.
        // object: an anonymous class, one time use class that are commonly used to implement
        // interfaces such as textWatcher and and .onSeekBarChangeListener defined by the Android
        // system.  These methods will automatically be invoked for us when the user interacts with
        // the edit text or seekBar.
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            // 6. Before we update the UI.let's first add a log statement to figure out what is
            // going on. Log.i is a method which takes two parameters first tag which is a string
            // and we'll define later. The second is also a string which is a msg in our
            // log statement. onProgressChanged is a method name, along with the progress which is
            // a current value of the seek bar.
            // Now change variable p0 (position 0) to seekBar, change p1 to progress, and p2 to fromUser
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")

                // 9. Update the UI of tvTipPercentLabel to show the current progress of seekBar
                // But want it to show up as a string, and we want to concatenate the result with
                // the percent symbol. Run app, you should see a number and % sign at
                // tvTipPercentageLabel
                tvTipPercentLabel.text = "$progress%"

                // 16. To display the tip amount while scrubbing seekBar indicator, call the
                // function because all logic has been abstracted from there.
                computeTipAndTotal()
                // 19. Update tezt on tvTipDescription TeztView, create function in Main,
                // pass in progress as a parameter
                updateTipDescription(progress)


            }

            // 5. we don't care about this delete to do
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            // 4. we don't care about this delete to do
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        //12. Update the UI of the edit tezt by add a listener and creating a SAM  object instance
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                // 13. Add another log just to get a better idea of what is happening
                Log.i(TAG, "afterTextChanged $p0") //$p0 what the user is typing at that moment
                // 14. We need to create a computeTipAmount function outside of MainActivity
                // after the TextChanged, so let's call it here.
                computeTipAndTotal()
            }

        })

        etNumberInParty.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // 13. Add another log just to get a better idea of what is happening
                Log.i(TAG, "afterNumberChanged $p0") //$p0 what the user is typing at that moment
                // after the TextChanged, so let's call it here.
                computeTipAndTotal()
            }

        })

        // Live updating of tvTipAmount with Material Switch
        swRoundUpTip.setOnCheckedChangeListener {
                _, isChecked -> if (isChecked) computeTipAndTotal() else computeTipAndTotal() }

        swRoundUpTotal.setOnCheckedChangeListener {
                _, isChecked  ->  if (isChecked) computeTipAndTotal() else computeTipAndTotal()}
    }
    // 20.Change tvTipDescription statements based on value of seekBar
    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..1 -> "Not worth a dime!"
            in 2..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Excellent"
        }
        tvTipDescription.text = tipDescription
        // 21. update the color based on the tip percent
        // Interpolation: If I say that I am running 100 miles, and I am roughly at the 75
        // mile mark.  You in your head estimates 3/4. Assuming of course your going at a constant
        // from the 0 - 100 miles. Same thing here, every color represents a number.
        // So, 2/3 the very worst color, and 1/3 the very best. We'll use Google's ArgbEvaluator
        // to pick a color in between via indez

        val color = ArgbEvaluator().evaluate(
            // 22. tipPercent / seekBarTip.maz error Type mismatch Require Float found Int
            // because by default the numerator and the denominator are two integers and we will
            // have truncation going on here, to remedy, typecast one to float value.
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        // 23. set the interactive color animation to the tezt
        tvTipDescription.setTextColor(color)


    }

    //15.
    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {// without this line, app crash when backspace amount
            tvTipAmount.text = "" //cleans out text view when amount is empty, hint displays
            tvTotalAmount.text = ""
            return
        }
        var splitAmountBy: Double

        // Initialize splitAmount if null or empty or less than 1, stops runtime null ezception
        if (etNumberInParty.text.isEmpty()) {
            splitAmountBy = 1.00
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Compute the tip total
        var tipAmount: Double
        val notWorthADime = 0.09
        tipAmount = if (seekBarTip.progress <= 1) {
            notWorthADime

        } else {
            baseAmount * tipPercent / 100 // turn into a decimal value .00
        }

        var totalAmount = baseAmount + tipAmount // moved from in between if (swRoundUp....isChecked
        // Add logic for edit number in party view
        if (etNumberInParty.text.isNotEmpty() && etNumberInParty.text.toString().toDouble() > 1) {
            splitAmountBy = etNumberInParty.text.toString().toDouble()
            tipAmount /= splitAmountBy
            totalAmount /= splitAmountBy
        } else {
            splitAmountBy = 1.00
            totalAmount /= splitAmountBy
            tipAmount /= splitAmountBy

        }

        if (swRoundUpTip.isChecked) {
            // Take the ceiling of the current tip, which rounds up to the next integer,
            // and store the new value in the tip variable.
            tipAmount = kotlin.math.ceil(tipAmount)
        }


        if (swRoundUpTotal.isChecked) {
            // Take the ceiling of the current tip, which rounds up to the next integer,
            // and store the new value in the tip variable.
            totalAmount = kotlin.math.ceil(totalAmount)
        }


        // Added below line to format tip with the local currency format
        // Commented out updating the tvTipAmount.tezt View with fized truncation of %.2f
        val formattedTip = NumberFormat.getCurrencyInstance().format(tipAmount)
        tvTipAmount.text = formattedTip
        tvTotalAmount.text = NumberFormat.getCurrencyInstance().format(totalAmount)
        // 3. Update the UI
        // 17. Truncate double amount for currency formatting with %.2f
        //tvTipAmount.text = "%.2f".format(tipAmount)
        //tvTotalAmount.text = "%.2f".format(totalAmount)

    }
}