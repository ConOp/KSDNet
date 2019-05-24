public class Project {
    private String name;
    private int maxGrade;
    private int courseid;
    private int deadline; //sets by a teacher, last day of a project upload
    private int remaining_time; //for uploading - time_limit
    private int upload_date;
    private int groupMembers; //number of team_members


    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(int maxGrade) {
        this.maxGrade = maxGrade;
    }

    public void saveAs(){}

    public void checkDeadline(){}

    public void setGroupMembers(){}
}

