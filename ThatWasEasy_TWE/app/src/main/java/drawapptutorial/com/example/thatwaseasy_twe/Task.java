package drawapptutorial.com.example.thatwaseasy_twe;

/**
 * Created by Christian Coreil on 2/24/2017.
 */

public class Task {
    private int id;
    private int minutesNum;
    private String taskName;
    private String taskDesc;
    private String urgencyType;
    private String isComplete;
    private int timerNum;

    public Task(){

    }

    public Task(int id, int minutesNum, String taskName, String taskDesc, String urgencyType, String isComplete, int timerNum){
        this.id = id;
        this.minutesNum = minutesNum;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.urgencyType = urgencyType;
        this.isComplete = isComplete;
        this.timerNum = timerNum;
    }

    public Task(int minutesNum, String taskName, String taskDesc, String urgencyType, String isComplete, int timerNum){
        this.minutesNum = minutesNum;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.urgencyType = urgencyType;
        this.isComplete = isComplete;
        this.timerNum = timerNum;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {return id;}

    public void setName(String name) {
        this.taskName = name;
    }
    public String getName() {return taskName;}


    public void setMinutes(int minutes) {
        this.minutesNum = minutes;
    }
    public int getMinutes() {return minutesNum;}

    public void setDesc(String desc) {
        this.taskDesc = desc;
    }
    public String getDesc() {return taskDesc;}

    public void setUrg(String urgency) {
        this.urgencyType = urgency;
    }
    public String getUrg() {return urgencyType;}

    public void setCompletion(String isComplete) {
        this.isComplete = isComplete;
    }
    public String getCompletion() {return isComplete;}

    public void setTimerNum(int timerNum) {
        this.timerNum = timerNum;
    }
    public int getTimerNum() {return timerNum;}

    @Override
    public String toString(){
        return getId() + " - " + getName();
    }
}
