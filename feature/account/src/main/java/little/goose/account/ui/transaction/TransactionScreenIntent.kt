package little.goose.account.ui.transaction

import little.goose.account.data.models.IconDisplayType
import java.math.BigDecimal
import java.util.Date

sealed class TransactionScreenIntent {
    sealed class TransactionOperation : TransactionScreenIntent() {
        data class Done(val money: BigDecimal) : TransactionOperation()
        data class Again(val money: BigDecimal) : TransactionOperation()
    }

    open class ChangeTransaction : TransactionScreenIntent() {
        open val type: Int? = null
        open val money: BigDecimal? = null
        open val content: String? = null
        open val description: String? = null
        open val time: Date? = null
        open val iconId: Int? = null

        data class Description(
            override val description: String
        ) : ChangeTransaction()

        data class Time(
            override val time: Date
        ) : ChangeTransaction()

        data class Money(
            override val money: BigDecimal
        ) : ChangeTransaction()

        data class Icon(
            override val iconId: Int,
            override val content: String
        ) : ChangeTransaction()

        data class Type(
            override val type: Int
        ) : ChangeTransaction()

        operator fun plus(other: ChangeTransaction): ChangeTransaction {
            val thisChanged = this
            return object : ChangeTransaction() {
                override val type: Int? = other.type ?: thisChanged.type
                override val money: BigDecimal? = other.money ?: thisChanged.money
                override val content: String? = other.content ?: thisChanged.content
                override val description: String? = other.description ?: thisChanged.description
                override val time: Date? = other.time ?: thisChanged.time
                override val iconId: Int? = other.iconId ?: thisChanged.iconId
            }
        }
    }

    data class ChangeIconDisplayType(
        val iconDisplayType: IconDisplayType
    ) : TransactionScreenIntent()
}