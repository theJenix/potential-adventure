package com.punventure.punadventure.event;

import java.util.List;

import com.punventure.punadventure.model.Note;

public class NotesEvent {

    private List<Note> notes;

    public NotesEvent(List<Note> notes) {
        this.notes = notes;
    }
    
    public List<Note> getNotes() {
        return notes;
    }
}
