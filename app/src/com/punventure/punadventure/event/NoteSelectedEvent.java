package com.punventure.punadventure.event;

import com.punventure.punadventure.model.Note;

public class NoteSelectedEvent {

    private Note note;

    public NoteSelectedEvent(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
