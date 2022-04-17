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

private const val INITIAL_TIP_PERCENT = 15
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    
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
        
        // pull out references to the member variables in onCreate
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        swRoundUpTip = findViewById(R.id.swRoundUpTip)
        swRoundUpTotal = findViewById(R.id.swRoundUpTotal)
        etNumberInParty = findViewById(R.id.etNumberInParty)


        // TODO Clean Up the lateinit variables if possible
        // TODO Assign hard coded variables to string resources
        seekBarTip.progress = INITIAL_TIP_PERCENT

        // show a percentage when activity first starts
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"

        // shows the value before user inputs amount
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")

                // Update the UI of tvTipPercentLabel to show the current progress of seekBar
                tvTipPercentLabel.text = "$progress%"

                // Display tip amount while scrubbing the seekbar
                computeTipAndTotal()

                // Update text on tvTipDescription
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        
        //Update the UI of the edit tezt by add a listener and creating a SAM
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }
        })

        etNumberInParty.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }
        })

        // Live updating of tvTipAmount with Material Switch
        swRoundUpTip.setOnCheckedChangeListener {
                _, isChecked -> computeTipAndTotal()
        }

        swRoundUpTotal.setOnCheckedChangeListener {
                _, isChecked  -> computeTipAndTotal()
        }
    }
    
    // Changes tvTipDescription statements based on value of seekBar
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

        // Google's ArgbEvaluator to pick a color in between via index
        val color = ArgbEvaluator().evaluate(
            // tipPercent / seekBarTip.max error Type mismatch Require Float found Int
            // because by default the numerator and the denominator are two integers and we will
            // have truncation going on here, to remedy, typecast one to float value.
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        // set the interactive color animation to the tezt
        tvTipDescription.setTextColor(color)
    }

    // TODO Functional Decomposition of computeTipAndTotal()
   
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
        
        // Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // Compute the tip total
        var tipAmount: Double
        val notWorthADime = 0.09
        
        tipAmount = if (seekBarTip.progress <= 1) {
            notWorthADime
        } else {
            baseAmount * tipPercent / 100 // turn into a decimal value .00
        }

        var totalAmount = baseAmount + tipAmount
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

        // TODO() refactor into a single function
        // Take the ceiling of the current tip, which rounds up to the next integer, and store the new value in the tip variable.
        tipAmount = roundUp(swRoundUpTip, tipAmount)

        // Take the ceiling of the current total, which rounds up to the next integer, and store the new value in the totalAmount variable.
        totalAmount = roundUp(swRoundUpTotal, totalAmount)
        
        // Added below line to format tip with the local currency format
        // Commented out updating the tvTipAmount.text View with fixed truncation of %.2f
        val formattedTip = NumberFormat.getCurrencyInstance().format(tipAmount)
        tvTipAmount.text = formattedTip
        tvTotalAmount.text = NumberFormat.getCurrencyInstance().format(totalAmount)
    }

    // Checks if switch is checked if so, round up to nearest integer, return amount
    fun roundUp(switch: SwitchMaterial, amount: Double): Double {

        if (switch.isChecked) {
            return kotlin.math.ceil(amount)
        } else {
            return amount
        }
    }

}
