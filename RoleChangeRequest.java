package model;

import java.util.ArrayList;
import java.util.List;

public class RoleChangeRequest {

    private String id;
    private Member requester;
    private Member target;
    private Role newRole;
    private RequestStatus status;

    private List<Decision> decisions;

    public RoleChangeRequest(
            String id,
            Member requester,
            Member target,
            Role newRole) {

        this.id = id;
        this.requester = requester;
        this.target = target;
        this.newRole = newRole;
        this.status = RequestStatus.PENDING;
        this.decisions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Member getRequester() {
        return requester;
    }

    public Member getTarget() {
        return target;
    }

    public Role getNewRole() {
        return newRole;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public List<Decision> getDecisions() {
        return decisions;
    }

    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }

    public void addDecision(Decision decision) {
        decisions.add(decision);
    }

    public boolean hasDecisionFrom(String memberId) {

        for (Decision decision : decisions) {

            if (decision.getMember().getId().equals(memberId)) {
                return true;
            }
        }

        return false;
    }

    public int countApprovals() {

        int count = 0;

        for (Decision decision : decisions) {

            if (decision.getResult() == DecisionResult.APPROVE) {
                count++;
            }
        }

        return count;
    }

    public int countRejections() {

        int count = 0;

        for (Decision decision : decisions) {

            if (decision.getResult() == DecisionResult.REJECT) {
                count++;
            }
        }

        return count;
    }

    public void approve() {
        status = RequestStatus.APPROVED;
    }

    public void reject() {
        status = RequestStatus.REJECTED;
    }

    public void cancel() {
        status = RequestStatus.CANCELLED;
    }
}