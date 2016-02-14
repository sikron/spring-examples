tbd

with curl

* get a token:
curl -u aClient:client_secret -X POST http://localhost:8080/oauth/token -H "Accept:application/json" -d "username=donald&password=d8ck&grant_type=password"
* access the resource:
curl http://localhost:8080/it -H "Authorization: Bearer 0645937d-56ea-4136-aae6-a8373a61c580"

with postman:

* basic auth : aClient:client_secret
* body must be provided as multipart formadata (form-url-encoded) 

resources:

* http://projects.spring.io/spring-security-oauth/docs/oauth2.html