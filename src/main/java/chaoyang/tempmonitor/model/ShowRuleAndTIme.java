package chaoyang.tempmonitor.model;

import java.util.List;

public class ShowRuleAndTIme {
    private List<Integer> ruleID;
    private String fixStartTime;

    public List<Integer> getRuleID() {
        return ruleID;
    }

    public void setRuleID(List<Integer> ruleID) {
        this.ruleID = ruleID;
    }

    public String getFixStartTime() {
        return fixStartTime;
    }

    public void setFixStartTime(String fixStartTime) {
        this.fixStartTime = fixStartTime;
    }
}
