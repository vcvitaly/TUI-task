# TUI-task

### About

This project allows you to receive information about user and his repositories from version control providers, 
at present GitHub.

In particular it does the following - given username list all user's github repositories, which are not forks. Information provided in the response, is:

1. Repository Name
2. Owner Login
3. For each branch it’s name and last commit sha

### Steps to run the application

Check that docker is installed:

```
docker -v
```
Build an image:

```
./scripts/build_image.sh
```
Create a github token with the repo scope, replace <your_token> with that token 
in the run script and run the image:
```
./scripts/run_app.sh
```
You can send requests to the app using cURL:
```
curl --location --request GET 'http://localhost:8080/api/v1/vcs-api/vcvitaly/repos/github'
```