package pan.lib.baseandroidframework

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * AUTHOR Pan Created on 2022/2/26
 */
@Parcelize
data class Book(var bookId: Int, var bookName: String) : Parcelable {

    override fun toString(): String {
        return "[bookId:$bookId, bookName:$bookName]"
    }
}