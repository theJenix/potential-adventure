class CreateNotes < ActiveRecord::Migration
  def change
    create_table :notes do |t|
      t.string :title
      t.string :note
      t.string :audio_path
      t.integer :latitude
      t.integer :longitude
      t.string :sender
      t.string :recipient
      t.integer :visible_range

      t.timestamps
    end
  end
end
