package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "imageUri")
    val imageUri: String,

    @ColumnInfo(name = "resultName")
    val resultName: String,

    @ColumnInfo(name = "resultScore")
    val resultScore: String
)