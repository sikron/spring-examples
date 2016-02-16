# password flow

* get a token:
curl -u aClient:client_secret -X POST http://localhost:8080/oauth/token -H "Accept:application/json" -d "username=donald&password=d8ck&grant_type=password"
* access the resource:
curl http://localhost:8080/it -H "Authorization: Bearer 0645937d-56ea-4136-aae6-a8373a61c580"

with postman:

* basic auth : aClient:client_secret
* body must be provided as multipart formadata (form-url-encoded) 

resources:

* http://projects.spring.io/spring-security-oauth/docs/oauth2.html
* http://www.baeldung.com/rest-api-spring-oauth2-angularjs

# refresh_token flow

resources:

* https://github.com/neel4software/SpringSecurityOAuth2

# implicit flow

http://localhost:8080/oauth/authorize?response_type=token&client_id=aClient&redirect_uri=http://localhost:8080
basic-authenticated with donald/d8ck

in a form the authorization can be granted, then the form-POST is forwarded to
http://localhost:8080/#access_token=143961e8-aac8-44e6-82a6-9a9abe5ff217&token_type=bearer&expires_in=43199&scope=read

hard to implement integration test, as user interaction is required and i currently don't know how to get and answer the form

resources:

* https://jaxenter.com/rest-api-spring-java-8-112289.html
* http://callistaenterprise.se/blogg/teknik/2015/04/27/building-microservices-part-3-secure-APIs-with-OAuth/

# authentication code flow

http://localhost:8080/oauth/authorize?response_type=code&client_id=aClient&redirect_uri=http://localhost:8080
basic-authenticated with donald/d8ck

in a form the authorization can be granted, then the form-POST is forwarded to
http://localhost:8080/?code=uB3q28

then
curl -u aClient:client_secret -X POST http://localhost:8080/oauth/token -H "Accept:application/json" -d "client_id=aClient&client_secret=client_secret&grant_type=authorization_code&code=ggHxIf&redirect_uri=http://localhost:8080"

response will be just like in the "password" flow:
{"access_token":"747df752-e9d0-4eed-b7d5-ef6a66fc393c","token_type":"bearer","refresh_token":"f6518739-7f35-4c89-a20e-a346b382efa8","expires_in":43199,"scope":"read write"}

hard to implement integration test, as user interaction is required and i currently don't know how to get and answer the form

resources:

* http://www.ibm.com/developerworks/library/se-oauthjavapt3/
