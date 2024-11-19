package main.java.javafx.model;

import java.time.LocalDate;

public class Employee {
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private String jobTitle;
    private float salary;
    private int departmentID;
	private String departmentName;

    public int getEmployeeID(){
		return ID;
	}
	public String getFirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName; 
	}
	public String getEmail(){
		return email;
	}
	public String getPhoneNum(){
		return phoneNumber;
	}
	public LocalDate getHireDate(){
		return hireDate;
	}
    public String getJobTitle(){
        return jobTitle;
    }
	public float getSalary(){
		return salary;
	}
	public int getDepartmentID(){
		return departmentID;
	}
	public String getDepartmentName(){
		return departmentName;
	}



	public void setEmployeeID(int ID){
		this.ID = ID;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName; 
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setPhoneNum(String phoneNum){
		this.phoneNumber = phoneNum;
	}
	public void setHireDate(LocalDate hireDate){
		this.hireDate = hireDate;
	}
    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;
    }
	public void setSalary(float salary){
		this.salary = salary;
	}
	public void setDepartmentID(int departmentID){
		this.departmentID = departmentID;
	}
	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName; 
	}

	
}

