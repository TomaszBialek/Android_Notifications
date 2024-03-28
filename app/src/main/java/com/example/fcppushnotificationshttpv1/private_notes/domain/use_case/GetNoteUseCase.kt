package com.example.fcppushnotificationshttpv1.private_notes.domain.use_case

import com.example.fcppushnotificationshttpv1.private_notes.domain.model.Note
import com.example.fcppushnotificationshttpv1.private_notes.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}