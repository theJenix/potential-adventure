package com.punventure.punadventure.model;


public class Note {

    private long id;
    private String title;
    private String note;
    private String audio_path = "";
    private double latitude;
    private double longitude;
    private String sender;
    private String recipient;
    private int visible_range;
    private String image_path = "";

    public Note() {
        // TODO Auto-generated constructor stub
    }

    public Note(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAudio_path() {
        return audio_path;
    }
    
    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }
    public String getImage_path() {
        return image_path;
    }
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public int getVisible_range() {
        return visible_range;
    }
    public void setVisible_range(int visible_range) {
        this.visible_range = visible_range;
    }
    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Note other = (Note) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public void resolveFiles() {
        // TODO Auto-generated method stub
        
    }    
}
