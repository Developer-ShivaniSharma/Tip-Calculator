@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.myapp.tipcalculator

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
    var tipInput by remember { mutableStateOf("") }
    val tipPercent: Double = tipInput.toDoubleOrNull() ?: 0.0
    //We have add new tip variable, call the calculate function and pass parameters amount and tipPercent.
    var roundUp by remember { mutableStateOf(false) }
    val tip: Double = calculateTip(amount, tipPercent, roundUp)


    Column(
        //The verticalScroll enables the column to scroll vertically.
        // The  rememberScrollState automatically remembers the scroll state
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 17.dp)
                .align(Alignment.Start)
        )
        EditableNumberField(
            label = R.string.bill_Amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            initialValue = amountInput,
            onInputChanged = { changedAmount ->
                amountInput = changedAmount
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()

        )
        EditableNumberField(
            label = R.string.how_the_service,
            //Keyboard.Default.copy(), The KeyboardOption  is the data class in which Default is the predefined object.
            //copy() is used if we want to add new things in the keyboard.
            //because in the data class we can add new things by using copy() function.
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            initialValue = tipInput,
            onInputChanged = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChange = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
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
@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double =15.0, roundUp: Boolean): Double {
    var tip = (tipPercent * amount) / 100
    // the kotlin.math.ceil roundup the value Exp: 10.6 will be round up as 11
    if(roundUp){
        tip = kotlin.math.ceil(tip)
    }
    return tip
}

// The FormatAmount function convert any amount into currency.
@VisibleForTesting
internal fun formatAmount(amounts: Double): String {
    //NumberFormat ,method is use to format the tip into currency
    return NumberFormat.getCurrencyInstance().format(amounts)
}

/**
 * The EditNumberField(), is used for both bill amount and Tip Amoumt calculation.
 * So we Add label in the parameter.
 * When we use this function in the [TipCalculator] the label value change according to the requirement.
 * But we can see the label is of type Int.So we use @StringRes annotation.
 * @StringRes is the typeSafe way to use String Resource. It shows the int to be passed is a string resource from value/string.xml.
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    initialValue: String,
    onInputChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = initialValue,
        // The onValueChange calls when the value changes.
        onValueChange = onInputChanged,
        // Now we use the label parameter using lambda expression.
        //We call the Text function that tales the string input of bill amount.
        label = { Text(stringResource(label)) },
        //Add the single line parameter which accepts the boolean value.
        singleLine = true,
        //Add keyboardOptions parameter that take KeyBoardOptions function.
        //The KeyBoard function gives the keyboard option or type to user to add string,Number,Url,Email etc.
        keyboardOptions = keyboardOptions, modifier = modifier
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean, onRoundUpChange: (Boolean) -> Unit, modifier: Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculatorTheme {
        TipCalculator()
    }
}