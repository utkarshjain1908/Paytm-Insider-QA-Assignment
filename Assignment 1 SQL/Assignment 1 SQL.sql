/* Create a table called Employee */
CREATE TABLE Employee(EmpID integer PRIMARY KEY, EmpName varchar, Date_of_birth Date);

/* Insert dummy data in Employee Table */
Insert Into Employee values(11, "Utkarsh", "1995/08/19");
Insert Into Employee values(12, "Rahul", "1996/02/13");
Insert Into Employee values(13, "Ishan", "1994/10/28");

/* Create a table called Salary */ 
CREATE TABLE Salary(EmpID integer PRIMARY KEY, Salary integer);

/* Insert dummy data in Slary Table */
Insert into Salary values(11, 25000);
Insert into Salary values(12, 26000);
Insert Into Salary values(13, 30000);

/* PART A- Write an SQL query to find nth largest salary along with employee name. */
Select b.Salary, a.EmpName from Employee a, Salary b where a.EmpID = b.EmpID and b.Salary = (Select MAX(Salary) from Salary);

/* PART B- Write a query to update salary of employees to 5000 whose age is 30+ */
MERGE into Salary USING Employee ON (Salary.EmpID = Employee.EmpID) WHEN MATCHED THEN UPDATE SET Salary.Salary = 5000 WHERE ((CURRENT_DATE - Employee.Date_of_birth)/365.25) > 30
