# Description

This is a CRUD application with a REST API architecture. 
The app's purpose is to help the user study for a test 
or simply get more knowledge about different topics.

The app satisfies the following use cases:

**User can perform CRUD operations on Categories**

**User can perform CRUD operations on Study Sessions**

**User can perform CRUD operations on Flashcards**

# Technologies:
Java 17, Spring Boot, Spring MVC, Spring JPA

# Prerequisites
1) JDK / Java 17
2) MySQL

# Instructions to run
1) Git clone
2) Create a database scheme with name 'flashcards' 
3) Change the following properties in the 'application.yml' file to match your local MySQL credentials: 
'spring.datasource.username' and 'spring.datasource.password' 
4) Run the app

# Testing
The app has a solid suite of unit tests and integration tests with (almost) 100% line coverage

# Swagger UI
The app includes Swagger UI documentation. 

Once you start the application, you can test it manually with this tool in the following path:

http://localhost:8080/swagger-ui/index.html

### Swagger UI Screenshots
![Swagger UI Caterogy resource](/src/main/resources/static/images/swagger-category.jpg)
![Swagger UI Study Session resource](/src/main/resources/static/images/swagger-study-session.jpg)
![Swagger UI Flashcard resource](/src/main/resources/static/images/swagger-flashcard.jpg)

# Sample data
When the app starts, it loads a set of sample data for demonstration purposes.
The loaded data includes the following:
1) Sample categories
2) Sample study sessions for each category
3) Sample flashcards for each study session

This data can be manipulated with the included Swagger UI or any HTTP client of your choice.

### Sample Categories
![Swagger UI Category sample data](/src/main/resources/static/images/swagger-categories.jpg)
### Sample Study Sessions
![Swagger UI Study Session sample data](/src/main/resources/static/images/swagger-study-sessions.jpg)
### Sample Flashcards
![Swagger UI Flashcard sample data](/src/main/resources/static/images/swagger-flashcards.jpg)


