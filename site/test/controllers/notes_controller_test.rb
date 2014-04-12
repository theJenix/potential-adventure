require 'test_helper'

class NotesControllerTest < ActionController::TestCase
  setup do
    @note = notes(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:notes)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create note" do
    assert_difference('Note.count') do
      post :create, note: { audio_path: @note.audio_path, id: @note.id, latitude: @note.latitude, longitude: @note.longitude, note: @note.note, recipient: @note.recipient, sender: @note.sender, title: @note.title, visible_range: @note.visible_range }
    end

    assert_redirected_to note_path(assigns(:note))
  end

  test "should show note" do
    get :show, id: @note
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @note
    assert_response :success
  end

  test "should update note" do
    patch :update, id: @note, note: { audio_path: @note.audio_path, id: @note.id, latitude: @note.latitude, longitude: @note.longitude, note: @note.note, recipient: @note.recipient, sender: @note.sender, title: @note.title, visible_range: @note.visible_range }
    assert_redirected_to note_path(assigns(:note))
  end

  test "should destroy note" do
    assert_difference('Note.count', -1) do
      delete :destroy, id: @note
    end

    assert_redirected_to notes_path
  end
end
