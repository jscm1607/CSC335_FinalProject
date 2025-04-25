## CSC 335 - Object-Oriented Programming and Design
## Final Project -- Restaurant
## Members
Gabe Venegas<br />
Aidan Fuhrmann<br />
Cody Rice<br />
Jose Santiago Campa Morales

## Design:
Our project follows a MVC strucutre to develop a restaurant 
manager software from a server's point of view. <br />

Oue model is made up of five distinct classes (Food, Order, 
OrderFood, Server and Session). Each model class has its respective 
Data Access Object (DAO) Class, which extends from the DAO class. 
An individual DAO class is in change of overriding the DAO methods, 
which perform basic SQL database operations. The model classes contain 
their DAO object, and all of the instance variables inside these classes 
are final, and our methods consist of setters and getters.<br />

The controller is made up of a Controller class which contains five DAO 
objects, one for each model class. The controller's main goal is to update 
the model while communicating with the GUI when a change occurs. The controller 
holds methods related to each model class, allowing us to retrieve information 
from the database.<br />

The view is made up of various graphic panels using Java Swing. Our main 
entry point is the login frame, where a server has the opportunity to either 
log into their shift or register themselves. After log in, the server is met 
with the main window, where other panels have different functions. Some include 
assigning tables, checking table availability, selecting seats for ordering, and 
analyzing sales. Our view was created with the help of generative AI.

## Instructions:
Make sure you run this program as a
Java Application from the LoginFrame.java file.
