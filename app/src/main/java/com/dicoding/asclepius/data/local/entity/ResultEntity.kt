package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "imageUri")
    val imageUri: String?,

    @ColumnInfo(name = "resultName")
    val resultName: String?,

    @ColumnInfo(name = "resultScore")
    val resultScore: String?
)