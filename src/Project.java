import java.sql.Date;
import java.time.LocalDate;

public class Project {
    private int projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int departmentId;

    // Getters and Setters
    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public int getDepartmentId() {
        return departmentId;
    }
    

    
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public void setDepartment(int departmentId) {
        this.departmentId = departmentId;
    }
    
    // Optional: toString for debugging or display purposes
    @Override
    public String toString() {
        return "Project{" +
        "projectId=" + projectId +
        ", projectName='" + projectName + '\'' +
        ", description='" + description + '\'' +
        ", startDate='" + startDate + '\'' +
        ", endDate='" + endDate + '\'' +
        ", departmentId=" + departmentId +
               '}';
    }
}
