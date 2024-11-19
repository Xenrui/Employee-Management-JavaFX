package main.java.javafx.model;

public class Department {
    private int id;
    private String name;
    private boolean isActive;
    private String description;


    public void setDepartmentID(int id){
        this.id = id;
    }

    public void setDepartmentName(String name){
        this.name = name;
    }

    public void setActive(Boolean isActive){
        this.isActive = isActive;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Boolean getActive(){
        return isActive;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
