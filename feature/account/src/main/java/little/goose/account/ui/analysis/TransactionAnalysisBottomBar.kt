package little.goose.account.ui.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import little.goose.account.ui.component.MonthSelector
import little.goose.account.ui.component.MonthSelectorState
import little.goose.account.ui.component.YearSelector
import little.goose.account.ui.component.YearSelectorState

data class TransactionAnalysisBottomBarState(
    val type: TransactionAnalysisViewModel.Type,
    val year: Int,
    val month: Int,
    val monthSelectorState: MonthSelectorState,
    val yearSelectorState: YearSelectorState,
    val onTypeChange: (type: TransactionAnalysisViewModel.Type) -> Unit
)

@Composable
fun TransactionAnalysisBottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 3.dp,
    state: TransactionAnalysisBottomBarState,
    shape: Shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
) {
    val windowInsets =
        WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        contentColor = containerColor,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            val selectorModifier = remember {
                Modifier
                    .padding(horizontal = 24.dp)
                    .height(42.dp)
                    .fillMaxWidth()
            }

            if (state.type == TransactionAnalysisViewModel.Type.YEAR) {
                YearSelector(
                    modifier = selectorModifier,
                    state = state.yearSelectorState,
                    shape = RoundedCornerShape(18.dp)
                )
            } else {
                MonthSelector(
                    modifier = selectorModifier,
                    state = state.monthSelectorState,
                    shape = RoundedCornerShape(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationBarItem(
                    selected = state.type == TransactionAnalysisViewModel.Type.YEAR,
                    onClick = { state.onTypeChange(TransactionAnalysisViewModel.Type.YEAR) },
                    icon = { Text(text = "${state.year}年", Modifier.padding(6.dp)) }
                )
                NavigationBarItem(
                    selected = state.type == TransactionAnalysisViewModel.Type.MONTH,
                    onClick = { state.onTypeChange(TransactionAnalysisViewModel.Type.MONTH) },
                    icon = { Text(text = "${state.month}月", Modifier.padding(6.dp)) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(widthDp = 380, heightDp = 200)
@Composable
private fun PreviewTransactionAnalysisBottomBar() {
    TransactionAnalysisBottomBar(
        modifier = Modifier.fillMaxSize(),
        state = TransactionAnalysisBottomBarState(
            TransactionAnalysisViewModel.Type.YEAR, 0, 0,
            MonthSelectorState(0, 0) { _, _ -> },
            YearSelectorState(0) {}
        ) {}
    )
}