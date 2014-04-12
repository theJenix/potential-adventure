class ChangeIntToDouble < ActiveRecord::Migration
  def change
    remove_column :notes, :latitude
    remove_column :notes, :longitude
    add_column :notes, :latitude, :float
    add_column :notes, :longitude, :float
  end
end
