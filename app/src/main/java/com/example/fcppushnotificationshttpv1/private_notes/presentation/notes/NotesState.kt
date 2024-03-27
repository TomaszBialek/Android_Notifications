package com.example.fcppushnotificationshttpv1.private_notes.presentation.notes

import com.example.fcppushnotificationshttpv1.private_notes.domain.model.Note
import com.example.fcppushnotificationshttpv1.private_notes.domain.util.NoteOrder
import com.example.fcppushnotificationshttpv1.private_notes.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
