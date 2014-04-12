class AddImage < ActiveRecord::Migration
  def change
    add_column :notes, :image_path, :string
  end
end
