package com.example.tippy

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.text.toInt as toInt1

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var sbTipPercent: SeekBar
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var tvSplitLabel: TextView
    private lateinit var tvSplitTipLabel: TextView
    private lateinit var tvSplitTotalLabel: TextView
    private lateinit var tvSplitNumber: TextView
    private lateinit var tvSplitTipAmount: TextView
    private lateinit var tvSplitTotalAmount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        sbTipPercent = findViewById(R.id.sbTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        tvSplitLabel = findViewById(R.id.tvSplitLabel)
        tvSplitTipLabel = findViewById(R.id.tvSplitTipLabel)
        tvSplitTotalLabel = findViewById(R.id.tvSplitTotalAmount)
        tvSplitNumber = findViewById(R.id.tvSplitNumber)
        tvSplitTipAmount = findViewById(R.id.tvSplitTipAmount)
        tvSplitTotalAmount = findViewById(R.id.tvSplitTotalAmount)

        sbTipPercent.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        sbTipPercent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })
        btnSplitIncrement.setOnClickListener {
            val updatedSplitNumber = tvSplitNumber.text.toString().toInt1() + 1
            tvSplitNumber.setText(updatedSplitNumber.toString())
            computeTipAndTotal()
        }
        btnSplitDecrement.setOnClickListener {
            var updatedSplitNumber = tvSplitNumber.text.toString().toInt1()
            if (updatedSplitNumber > 1) {
                updatedSplitNumber -= 1
            }
            tvSplitNumber.setText(updatedSplitNumber.toString())
            computeTipAndTotal()
        }
    }

    private fun updateTipDescription(tipPercent: Int) {
        tvTipDescription.text = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        // Update tvTipDescription TextColor
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / sbTipPercent.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvSplitTipAmount.text = ""
            tvSplitTotalAmount.text = ""
            return
        }
        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = sbTipPercent.progress
        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Compute the splitTip and splitTotal
        val splitTip = tipAmount / tvSplitNumber.text.toString().toInt1()
        val splitTotal = totalAmount / tvSplitNumber.text.toString().toInt1()
        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvSplitTipAmount.text = "%.2f".format(splitTip)
        tvSplitTotalAmount.text = "%.2f".format(splitTotal)
    }
}