package com.example.t1

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.time.temporal.TemporalAmount

private const val TAG = "MainActivity"
private const val INIT_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipRate: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipRate = findViewById(R.id.tvTipRate)

        seekBarTip.progress = INIT_TIP_PERCENT
        tvTipPercentLabel.text = "$INIT_TIP_PERCENT%"
        updateTipRate(INIT_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipRate(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })

    }

    private fun updateTipRate(tipPercent: Int) {
        val tipRate = when (tipPercent) {
            in 0..5 -> "\uD83D\uDE1E"
            in 6..9 -> "\uD83D\uDE1F"
            in 10..19 -> "\uD83D\uDE10"
            in 20..25 -> "\uD83D\uDE42"
            else -> "\uD83D\uDE0A"
        }
        tvTipRate.text = tipRate
        // update color based on tipPercent
        var color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.worst_tip),
            ContextCompat.getColor(this, R.color.best_tip)
        ) as Int
        tvTipPercentLabel.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        // 1. Get the values
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        // 2. Calc tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // Log.i(TAG, "Tip ${tipAmount.toString()} and Total ${totalAmount.toString()}")
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}