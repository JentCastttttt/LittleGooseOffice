package little.goose.note.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import little.goose.note.data.constants.TABLE_NOTE
import java.util.*

@Parcelize
@Entity(tableName = TABLE_NOTE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String,
    var content: String,
    var time: Date = Date()
) : Parcelable {
    constructor(): this(null, "", "", Date())
}