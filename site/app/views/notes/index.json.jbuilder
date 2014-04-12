json.array!(@notes) do |note|
  json.extract! note, :id, :id, :title, :note, :audio_path, :latitude, :longitude, :sender, :recipient, :visible_range
  json.url note_url(note, format: :json)
end
