package com.example.tippytotal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

//private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    // 1. Create member variables, late initialization in onCreate not in the Main Constructor
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

        //3. We get notified by the seek bar if we add a listener to it
        // tell the android system what to do, when something has happened,
        // by defining  this class inside the parentheses
        // object will get a red squiggly, click on light bulb, click implement members
        // to override 3 methods that must now be changed to comply with the definition of the
        // onSeekBarChangeListener.
        //We are defining an anonymous class which implements this interface.
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            // 6. Before we update the UI.let's first add a log statement to figure out what is
            // going on. Log.i is a method which takes two parameters first tag which is a string
            // and we'll define later. The second is also a string which is a msg in our
            // log statement. onProgressChanged is a method name, along with the progress which is
            // a current value of the seek bar.
            // Now change variable p0 to seekBar, change p1 to progress, and p2 to fromUser
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(tag, "onProgressChanged $progress")
            }

            // 5. we don't care about this delete to do
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            // 4. we don't care about this delete to do
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }
}