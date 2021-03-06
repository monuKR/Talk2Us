package com.talk2us.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talk2us.R
import com.talk2us.utils.Constants
import com.talk2us.utils.PrefManager


@Entity(tableName = "messages")
data class Message(
    @ColumnInfo(name = "message")
    val word: String,
    @PrimaryKey
    val timeStamp: String,
    var sent: Boolean,
    val seen: Boolean,
    val sentFrom: String,
    var messageId: String = PrefManager.getString(
        Constants.COUNSELLOR_ID,
        Constants.NOT_DEFINED
    ) + PrefManager.getString(Constants.CLIENT_ID, Constants.NOT_DEFINED)

) {
    constructor() : this(
        "",
        "",
        false,
        false,
        "Client",
        PrefManager.getString(
            Constants.COUNSELLOR_ID,
            Constants.NOT_DEFINED
        ) + PrefManager.getString(Constants.CLIENT_ID ,Constants.NOT_DEFINED)
    )
}
