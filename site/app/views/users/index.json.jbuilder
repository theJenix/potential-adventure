json.array!(@users) do |user|
  json.extract! user, :id, :id, :uname
  json.url user_url(user, format: :json)
end
