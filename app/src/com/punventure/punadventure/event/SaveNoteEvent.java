package com.punventure.punadventure.event;

import com.punventure.punadventure.model.Note;

public class SaveNoteEvent {

    private Note note;

    public SaveNoteEvent(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
