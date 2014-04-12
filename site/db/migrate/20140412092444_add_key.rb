class AddKey < ActiveRecord::Migration
  def change
    remove_column :notes, :id
    add_column :notes, :id, :primary_key
  end
end
