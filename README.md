 Technologies Used

Java 21

Spring Boot

Spring Web

Spring Data JPA

Spring Validation

H2 In-Memory Database

Maven for build automation

JUnit 5 for unit testing

 Project Setup Instructions
1️ Prerequisites

Before running the project, make sure you have installed:

Java 21+

Maven 3.9+

Any IDE (Eclipse, IntelliJ, VS Code)

Environment Setup
Clone the Repository
git clone https://github.com/NikolaOpacicc/canteen-reservation.git
cd canteen-reservation
 Build Instructions

To build the project, run:

mvn clean install

or, if you only want to compile without running tests:

mvn clean compile
Running the Application

Run the Spring Boot application using:

mvn spring-boot:run

After startup, the application will be available at:

http://localhost:8080

The application uses an in-memory H2 database, which resets on each restart.
