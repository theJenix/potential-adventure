class NotesController < ApplicationController
  include NotesHelper
  before_action :set_note, only: [:show, :edit, :update, :destroy]

  # GET /notes
  # GET /notes.json
  def index_json
    @ni = Note.all
    p = params()
    if (p.has_key?("lat") && p.has_key?("lon"))
      @ni = notes_in_range(p['lat'].to_f, p['lon'].to_f)
    end
    @notes = @ni

    render :json => @notes.to_json( )
  end

  def index
    @ni = Note.all
    p = params()
    if (p.has_key?("lat") && p.has_key?("lon"))
      @ni = notes_in_range(p['lat'].to_f, p['lon'].to_f)
    end
    @notes = @ni
  end
  def index_hack
    @ni = Note.all
    p = params()
    if (p.has_key?("lat") && p.has_key?("lon"))
      @ni = notes_in_range(p['lat'].to_f, p['lon'].to_f)
    end
    @notes = {
      :notes => @ni
    }
    respond_to do |format|
      format.json { render json: @notes}
    end
  end

  def image
    n = Note.find(params[:id])
    name = params[:fileName]
    directory = "public/images"
    path = File.join(directory, name)
    File.open(path, "wb") { |f| f.write(params[:image].read) }
    n.image_path = directory + "/" + name
    n.save();
  end


  # GET /notes/1
  # GET /notes/1.json
  def show
  end

  # GET /notes/new
  def new
    @note = Note.new
  end

  # GET /notes/1/edit
  def edit
  end

  # POST /notes
  # POST /notes.json
  def create
    @note = Note.new(note_params)

    respond_to do |format|
      if @note.save
        format.html { redirect_to @note, notice: 'Note was successfully created.' }
        format.json { render action: 'show', status: :created, location: @note }
      else
        format.html { render action: 'new' }
        format.json { render json: @note.errors, status: :unprocessable_entity }
      end
    end
  end

  def create_app
    choices = params.select { |key, value| key.to_s != "id" and key.to_s != "action" and key.to_s != "controller" }
    @note = Note.new(choices)

    respond_to do |format|
      if @note.save
        format.html { redirect_to @note, notice: 'Note was successfully created.' }
        format.json { render action: 'show', status: :created, location: @note }
      else
        format.html { render action: 'new' }
        format.json { render json: @note.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /notes/1
  # PATCH/PUT /notes/1.json
  def update
    respond_to do |format|
      if @note.update(note_params)
        format.html { redirect_to @note, notice: 'Note was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: 'edit' }
        format.json { render json: @note.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /notes/1
  # DELETE /notes/1.json
  def destroy
    @note.destroy
    respond_to do |format|
      format.html { redirect_to notes_url }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_note
      @note = Note.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def note_params
      params.permit(:id, :title, :note, :audio_path, :latitude, :longitude, :sender, :recipient, :visible_range)
    end
end
