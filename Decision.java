package model;

public class Decision {

    private RoleChangeRequest request;
    private Member member;
    private DecisionResult result;

    public Decision(
            RoleChangeRequest request,
            Member member,
            DecisionResult result) {

        this.request = request;
        this.member = member;
        this.result = result;
    }

    public RoleChangeRequest getRequest() {
        return request;
    }

    public Member getMember() {
        return member;
    }

    public DecisionResult getResult() {
        return result;
    }
}