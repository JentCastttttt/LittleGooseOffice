package little.goose.account

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import little.goose.account.logic.AccountRepository
import little.goose.account.ui.transaction.TransactionHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountModule {

    @Provides
    @Singleton
    fun provideAccountRepository(application: Application): AccountRepository {
        return AccountRepository(application)
    }

    @Provides
    @Singleton
    fun provideTransactionHelper(accountRepository: AccountRepository): TransactionHelper {
        return TransactionHelper(accountRepository)
    }
}