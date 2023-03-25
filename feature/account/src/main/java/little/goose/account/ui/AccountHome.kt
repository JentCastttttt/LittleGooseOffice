package little.goose.account.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import little.goose.account.data.entities.Transaction
import little.goose.account.ui.component.AccountTitle
import little.goose.account.ui.component.AccountTitleState
import little.goose.account.ui.component.MonthSelectorState
import little.goose.account.ui.component.TransactionColumn
import little.goose.account.ui.transaction.TransactionDialog
import little.goose.account.ui.transaction.rememberTransactionDialogState

@Composable
fun AccountHome(
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel<AccountFragmentViewModel>()
    val transactions by viewModel.curMonthTransactionWithTime.collectAsState()
    val accountTitleState by viewModel.accountTitleState.collectAsState()
    val monthSelectorState by viewModel.monthSelectorState.collectAsState()
    val transactionDialogState = rememberTransactionDialogState()

    AccountScreen(
        modifier = modifier,
        transactionsWithTime = transactions,
        accountTitleState = accountTitleState,
        onTransactionClick = transactionDialogState::show,
        monthSelectorState = monthSelectorState,
    )

    TransactionDialog(
        state = transactionDialogState,
        onDelete = viewModel::deleteTransaction
    )
}

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    transactionsWithTime: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    accountTitleState: AccountTitleState,
    monthSelectorState: MonthSelectorState
) {
    Column(modifier) {
        AccountTitle(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            accountTitleState = accountTitleState,
            monthSelectorState = monthSelectorState
        )
        TransactionColumn(
            modifier = Modifier.weight(1F),
            transactions = transactionsWithTime,
            onTransactionClick = onTransactionClick
        )
    }
}