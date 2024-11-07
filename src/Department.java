
public class Department {
    private int id;
    private String name;
    private boolean isActive;


    public void setDepartmentID(int id){
        this.id = id;
    }

    public void setDepartmentName(String name){
        this.name = name;
    }

    public void setActive(Boolean isActive){
        this.isActive = isActive;
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

    
    @Override
    public String toString() {
        return name;
    }
    
}
