module NotesHelper
  def ll_distance(lat1, lon1, lat2, lon2)
    r = 6371 # In KM
    dist_lat = (lat2-lat1) * Math::PI / 180
    dist_lon = (lon2-lon1) * Math::PI / 180
    a = Math.sin(dist_lat/2) * Math.sin(dist_lat/2) + Math.cos(lat1 * Math::PI / 180 ) * Math.cos(lat2 * Math::PI / 180 ) * Math.sin(dist_lon/2) * Math.sin(dist_lon/2)
    c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
    d = r * c # Distance between them in km.
    return d * 1000 # Distance in Meters.
  end

  def notes_in_range(lat, lon)
    nir_id = Array.new
    @ni.each do |note|
      if (ll_distance(lat, lon, note.latitude, note.longitude) < note.visible_range)
        nir_id.append(note.id)
      end
    end
    return Note.find(nir_id)
  end
end
