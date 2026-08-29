package controller;

import java.util.List;

import model.DecisionResult;
import model.Member;
import model.Role;
import model.RoleChangeRequest;
import repository.MemberRepository;
import repository.RequestRepository;
import service.RoleChangeService;

public class RoleChangeController {

    private RoleChangeService service;
    private MemberRepository memberRepository;
    private RequestRepository requestRepository;

    public RoleChangeController(
            RoleChangeService service,
            MemberRepository memberRepository,
            RequestRepository requestRepository) {

        this.service = service;
        this.memberRepository = memberRepository;
        this.requestRepository = requestRepository;
    }

    public List<Member> getMembers() {

        return memberRepository.findAll();
    }

    public String createRequest(
            String requestId,
            String requesterId,
            String targetId,
            Role newRole) {

        return service.createRequest(
                requestId,
                requesterId,
                targetId,
                newRole
        );
    }

    public List<RoleChangeRequest> getRequests() {

        return requestRepository.findAll();
    }

    public String submitDecision(
            String requestId,
            String memberId,
            DecisionResult result) {

        return service.submitDecision(
                requestId,
                memberId,
                result
        );
    }

    public String cancelRequest(
            String requestId,
            String memberId) {

        return service.cancelRequest(
                requestId,
                memberId
        );
    }
}