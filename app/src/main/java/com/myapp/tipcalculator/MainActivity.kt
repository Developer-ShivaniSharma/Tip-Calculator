@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.myapp.tipcalculator

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.myapp.tipcalculator.ui.theme.TipCalculatorTheme
import androidx.compose.ui.unit.dp as dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculator()
                }
            }
        }
    }
}

@Composable
fun TipCalculator(modifier: Modifier = Modifier) {
    //The amountInput variable holds the state to display in UI.
    //We have use remember and mutableState.
    // the value get updated each time so we have use mutable state and remember is use to hold that value and update accordingly.
    var amountInput by remember { mutableStateOf("") }
    //We have add the amount variable in which we convert the user input value(String) to double or Null.
    // we have added the elvis operator ?:
    //If the user entered any invalid value then return null. The elvis operator calls and print 0.0
    val amount: Double = amountInput.toDoubleOrNull() ?: 0.0
    //We have add new tip variable which stores the calculate function and pass the amount.
    val tip: Double = calculateTip(amount)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 17.dp)
                .align(Alignment.Start)
        )
        EditNumberField(
            value = amountInput, onValueChange = { changedAmount ->
                amountInput = changedAmount
            }, modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.tip_amount, formatAmount(tip)),
            style = MaterialTheme.typography.displaySmall,
        )
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = stringResource(R.string.total_amount, amount, tip, amount + tip),
            style = MaterialTheme.typography.displaySmall,


            )
        Spacer(modifier = Modifier.height(150.dp))
    }

}

//This calculateTip Function is use to calculate the Tip amount.
private fun calculateTip(amt: Double, tipPercent: Double = 15.0): Double {
    return (tipPercent * amt) / 100
}

// The FormatAmount function convert any amount into currency.
private fun formatAmount(amounts: Double): String {
    //NumberFormat ,method is use to format the tip into currency
    return NumberFormat.getCurrencyInstance().format(amounts)
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = value,
        // The onValueChange calls when the value changes.
        onValueChange = onValueChange,
        // Now we use the label parameter using lambda expression.
        //We call the Text function that tales the string input of bill amount.
        label = { Text(stringResource(R.string.bill_Amount)) },
        //Add the single line parameter which accepts the boolean value.
        singleLine = true,
        //Add keyboardOptions parameter that take KeyBoardOptions function.
        //The KeyBoard function gives the keyboard option or type to user to add string,Number,Url,Email etc.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculatorTheme {
        TipCalculator()
    }
}