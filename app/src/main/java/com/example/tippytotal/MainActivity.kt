package com.example.tippytotal

import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView


//private const val TAG = "MainActivity"
// 11. Show a percentage when the activity first starts
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    // 1. Create member variables, late initialization in onCreate not in the Main Constructor
    // 8. Add tag for log, and run, open logcat, type MainActivity in search bar, and scrub
    // the seeBar you should see the value go from 1 - 30 in logcat
    val tag = "MainActivity"
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //2. pull out references to the member variables in onCreate
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)

        //12. Assign constant value to seekBar indicator, placing it in the center of its width
        // when activity first starts,
        seekBarTip.progress = INITIAL_TIP_PERCENT

        //13. Assign constant value to show a percentage when activity first starts
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"

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
                Log.i(tag, "onProgressChanged $progress")
                // 9. Update the UI of tvTipPercentLabel to show the current progress of seekBar
                // But want it to show up as a string, and we want to concatenate the result with
                // the percent symbol. Run app, you should see a number and % sign at
                // tvTipPercentageLabel
                tvTipPercentLabel.text = "$progress%"

                // 16. To display the tip amount while scrubbing seekBar indicator, call the
                // function because all logic has been abstracted from there.
                computeTipAndTotal()


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
                Log.i(tag, "afterTextChanged $p0") //$p0 what the user is typing at that moment
                // 14. We need to create a computeTipAmount function outside of MainActivity
                // after the TextChanged, so let's call it here.
                computeTipAndTotal()
            }

        })
    }
    //15.
    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {// without this line, app crash when backspace amount
            tvTipAmount.text = "" //cleans out text view when amount is empty, hint displays
            tvTotalAmount.text = ""
            return
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Compute the tip total
        val tipAmount = baseAmount * tipPercent / 100 // turn into a decimal value .00
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI
        // 17. Truncate double amount for currency formatting with %.2f
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}