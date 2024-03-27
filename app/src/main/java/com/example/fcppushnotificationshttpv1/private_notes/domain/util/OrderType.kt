package com.example.fcppushnotificationshttpv1.private_notes.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}