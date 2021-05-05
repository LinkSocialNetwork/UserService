# Link Social Media

## Project Description
Link is the second iteration of a social media application created by Team Avatar as part of the Revature Full Stack Angular training program in April 2021. Developed with a microservice architecture, this web application allows every user to follow each other and view each other's posts. Each user has their own account that they can customize with their own information. Within this network, users can interact with each other through comments, likes, and a global chatroom. This social media application is meant to ease the transition of becoming a Revature employee.

UserService handles the management of user data. It handles registration/login/logout functionality using JSON Web Tokens (JWTs) to authorize users and track online activity. It sends emails to guide users through password reset processes. It also allows users to update their profiles and protects sensitive data from being accessed by unauthorized users.

## Technologies Used

- Spring Boot
	- Euereka Discovery Client
	- Spring Boot Actuator
	- Spring Web MVC
	- Spring Mail
	- Lombok
	- Java Persistence API
- Java - version 1.8
- PostgrSQL - version 13.2
- auth0 JSON Web Token
- AWS Java S3 SDK
- JUnit
- Log4J
- H2 Database Testing

## Features

- Account registration with MD5 hashed password.
- Login / Logout management with JWT authentication.
- View user profiles.
- Update all fields of user profile (including unique fields such as username).
- Sends email messages during password reset process.
- Protects routes from unauthorized accesses with JWT checks.

## Getting Started
   
> Clone this repository
```
git clone https://github.com/LinkSocialNetwork/UserService.git
```

> Clone Eureka and Gateway services
```
git clone https://github.com/LinkSocialNetwork/Eureka.git
git clone https://github.com/LinkSocialNetwork/Gateway.git
```

> Clone FrontendClient
```
git clone https://github.com/LinkSocialNetwork/FrontendClient.git
```

> npm install in angular project folder
```
npm i FrontendClient/Angular
```

## **Usage**

> Run the services together, sequentially in an IDE
```
Eureka > Gateway > UserService
```

> Visit the Eureka url and confirm Eureka can see the Gateway and UserService
```
http://localhost:9999/Eureka
```


## **License**

This project uses the following license: [<The MIT License>](https://www.mit.edu/~amini/LICENSE.md).

