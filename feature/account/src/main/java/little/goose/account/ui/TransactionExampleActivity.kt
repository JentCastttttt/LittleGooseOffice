package little.goose.account.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import little.goose.account.data.constants.MoneyType
import little.goose.account.data.entities.Transaction
import little.goose.account.ui.component.TransactionColumn
import little.goose.account.ui.transaction.TransactionActivity
import little.goose.account.ui.transaction.TransactionDialog
import little.goose.account.ui.transaction.rememberTransactionDialogState
import little.goose.common.constants.KEY_CONTENT
import little.goose.common.constants.KEY_MONEY_TYPE
import little.goose.common.constants.KEY_TIME
import little.goose.common.constants.KEY_TIME_TYPE
import little.goose.common.dialog.time.TimeType
import little.goose.design.system.component.dialog.DeleteDialog
import little.goose.design.system.component.dialog.rememberDialogState
import little.goose.design.system.theme.AccountTheme
import little.goose.design.system.theme.Red200
import java.io.Serializable
import java.util.*

@AndroidEntryPoint
class TransactionExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountTheme {
                val viewModel = hiltViewModel<TransactionExampleViewModel>()
                val transactions by viewModel.transactions.collectAsState()
                val transactionDialogState = rememberTransactionDialogState()
                val deleteDialogState = rememberDialogState()
                val snackbarHostState = remember { SnackbarHostState() }
                var deletingTransaction: Transaction? by remember { mutableStateOf(null) }

                TransactionTimeScreen(
                    modifier = Modifier.fillMaxSize(),
                    title = viewModel.title,
                    onTransactionClick = transactionDialogState::show,
                    snackbarHostState = snackbarHostState,
                    snackbarAction = {
                        deletingTransaction?.let {
                            viewModel.insertTransaction(it)
                        }
                    },
                    transactions = transactions,
                    onBack = ::finish
                )

                TransactionDialog(
                    state = transactionDialogState,
                    onEditClick = {
                        TransactionActivity.openEdit(this, it)
                    },
                    onDeleteClick = {
                        deletingTransaction = it
                        deleteDialogState.show()
                    }
                )

                DeleteDialog(
                    state = deleteDialogState,
                    onCancel = {
                        deleteDialogState.dismiss()
                        deletingTransaction = null
                    },
                    onConfirm = {
                        deletingTransaction?.let {
                            viewModel.deleteTransaction(it)
                        }
                    }
                )

                LaunchedEffect(viewModel.event) {
                    viewModel.event.collect { event ->
                        when (event) {
                            is TransactionExampleViewModel.Event.DeleteTransaction -> {
                                launch {
                                    snackbarHostState.showSnackbar(
                                        message = getString(little.goose.common.R.string.deleted),
                                        actionLabel = getString(little.goose.common.R.string.undo),
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Indefinite
                                    )
                                }
                                launch {
                                    delay(2000L)
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    deletingTransaction = null
                                }
                            }
                            is TransactionExampleViewModel.Event.InsertTransaction -> {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                deletingTransaction = null
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun open(
            context: Context,
            time: Date,
            timeType: TimeType,
            moneyType: MoneyType = MoneyType.BALANCE,
            keyContent: String? = null
        ) {
            val intent = Intent(context, TransactionExampleActivity::class.java).apply {
                putExtra(KEY_TIME, time as Serializable)
                putExtra(KEY_TIME_TYPE, timeType as Parcelable)
                putExtra(KEY_MONEY_TYPE, moneyType as Parcelable)
                putExtra(KEY_CONTENT, keyContent)
            }
            context.startActivity(intent)
        }
    }
}

@Composable
private fun TransactionTimeScreen(
    modifier: Modifier = Modifier,
    title: String,
    transactions: List<Transaction>,
    snackbarHostState: SnackbarHostState,
    snackbarAction: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = Modifier,
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = little.goose.common.R.drawable.icon_back),
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        snackbarHost = {
            DeleteSnackbarHost(
                action = snackbarAction,
                snackbarHostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        TransactionColumn(
            modifier = Modifier.padding(paddingValues),
            transactions = transactions,
            onTransactionClick = onTransactionClick
        )
    }
}


@Composable
fun DeleteSnackbarHost(
    modifier: Modifier = Modifier,
    action: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        Snackbar(modifier = modifier.padding(12.dp), action = {
            snackbarData.visuals.actionLabel?.let { label ->
                TextButton(onClick = action) {
                    Text(text = label, color = Red200)
                }
            }
        }) {
            Text(text = snackbarData.visuals.message, color = Color.White)
        } // Snackbar
    } // SnackbarHost
}