import java.time.LocalDate;

public class Project {
    private int projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

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
    
    public boolean isActive() {
        return isActive;
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
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
        ", isActive=" + isActive +
               '}';
    }
}
