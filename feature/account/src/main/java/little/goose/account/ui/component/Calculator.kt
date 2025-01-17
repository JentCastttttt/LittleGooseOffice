package little.goose.account.ui.component

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import little.goose.account.R
import little.goose.account.logic.MoneyCalculatorLogic
import little.goose.design.system.theme.AccountTheme

@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    onNumClick: (num: Int) -> Unit,
    onAgainClick: () -> Unit,
    onDoneClick: () -> Unit,
    onOperatorClick: (MoneyCalculatorLogic) -> Unit,
    isContainOperator: Boolean
) {
    Column(modifier = modifier) {
        val context = LocalContext.current
        val vibrator = remember(context) {
            runCatching {
                context.getSystemService(Vibrator::class.java)
            }.getOrNull()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 7..9) {
                Cell(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                        onNumClick(num)
                    }
                ) {
                    Text(text = num.toString())
                }
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onOperatorClick(MoneyCalculatorLogic.BACKSPACE)
                }
            ) {
                Icon(imageVector = Icons.Rounded.Backspace, contentDescription = "BackSpace")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 4..6) {
                Cell(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                        onNumClick(num)
                    }
                ) {
                    Text(text = num.toString())
                }
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onOperatorClick(MoneyCalculatorLogic.Operator.PLUS)
                }
            ) {
                Text(text = "+")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (num in 1..3) {
                Cell(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                        onNumClick(num)
                    }
                ) {
                    Text(text = num.toString())
                }
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onOperatorClick(MoneyCalculatorLogic.Operator.SUB)
                }
            ) {
                Text(text = "-")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onOperatorClick(MoneyCalculatorLogic.DOT)
                }
            ) {
                Text(text = ".")
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onNumClick(0)
                }
            ) {
                Text(text = "0")
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    onAgainClick()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.next_transaction),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Cell(
                modifier = Modifier.weight(1F),
                onClick = {
                    vibrator?.vibrate(VibrationEffect.createOneShot(16, 180))
                    if (isContainOperator) {
                        onOperatorClick(MoneyCalculatorLogic.Operator.RESULT)
                    } else {
                        onDoneClick()
                    }
                }
            ) {
                Text(
                    text = if (isContainOperator) "=" else stringResource(id = R.string.done),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun Cell(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) = Surface(modifier = modifier, onClick = onClick) {
    ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Preview(heightDp = 380)
@Composable
fun PreviewCalculator() = AccountTheme {
    Calculator(
        onNumClick = {},
        onAgainClick = {},
        onDoneClick = {},
        onOperatorClick = {},
        isContainOperator = false
    )
}