package service;

import model.Decision;
import model.DecisionResult;
import model.Member;
import model.Role;
import model.RoleChangeRequest;
import repository.DecisionRepository;
import repository.MemberRepository;
import repository.RequestRepository;

public class RoleChangeService {

    private MemberRepository memberRepository;
    private RequestRepository requestRepository;
    private DecisionRepository decisionRepository;

    public RoleChangeService(
            MemberRepository memberRepository,
            RequestRepository requestRepository,
            DecisionRepository decisionRepository) {

        this.memberRepository = memberRepository;
        this.requestRepository = requestRepository;
        this.decisionRepository = decisionRepository;
    }

    public String createRequest(
            String requestId,
            String requesterId,
            String targetId,
            Role newRole) {

        Member requester =
                memberRepository.findById(requesterId);

        if (requester == null) {
            return "ปฏิเสธ: ไม่พบสมาชิกผู้เสนอ";
        }

        Member target =
                memberRepository.findById(targetId);

        if (target == null) {
            return "ปฏิเสธ: ไม่พบสมาชิกเป้าหมาย";
        }

        if (!requester.isActive()) {
            return "ปฏิเสธ: ผู้เสนอไม่ใช่สมาชิก Active";
        }

        if (requesterId.equals(targetId)) {
            return "ปฏิเสธ: ไม่สามารถสร้างคำขอให้ตัวเองได้";
        }

        if (requestRepository.findPendingByTarget(targetId) != null) {
            return "ปฏิเสธ: สมาชิกเป้าหมายมีคำขอที่รอพิจารณาอยู่แล้ว";
        }

        RoleChangeRequest request =
                new RoleChangeRequest(
                        requestId,
                        requester,
                        target,
                        newRole
                );

        requestRepository.save(request);

        return "สร้างคำขอสำเร็จ";
    }

    public String submitDecision(
            String requestId,
            String memberId,
            DecisionResult result) {

        RoleChangeRequest request =
                requestRepository.findById(requestId);

        if (request == null) {
            return "ปฏิเสธ: ไม่พบคำขอ";
        }

        if (!request.isPending()) {
            return "ปฏิเสธ: คำขอนี้สิ้นสุดแล้ว";
        }

        Member member =
                memberRepository.findById(memberId);

        if (member == null) {
            return "ปฏิเสธ: ไม่พบสมาชิก";
        }

        if (!member.isActive()) {
            return "ปฏิเสธ: สมาชิกไม่มีสิทธิ์ลงความเห็น";
        }

        if (member.getId().equals(
                request.getRequester().getId())) {

            return "ปฏิเสธ: ผู้เสนอคำขอไม่มีสิทธิ์ลงความเห็น";
        }

        if (member.getId().equals(
                request.getTarget().getId())) {

            return "ปฏิเสธ: สมาชิกเป้าหมายไม่มีสิทธิ์ลงความเห็น";
        }

        if (request.hasDecisionFrom(memberId)) {
            return "ปฏิเสธ: สมาชิกลงความเห็นต่อคำขอนี้ไปแล้ว";
        }

        Decision decision =
                new Decision(
                        request,
                        member,
                        result
                );

        request.addDecision(decision);

        decisionRepository.save(decision);

        if (request.countApprovals() >= 2) {

            request.approve();

            request.getTarget().changeRole(
                    request.getNewRole()
            );

            return "อนุมัติคำขอสำเร็จ";
        }

        if (request.countRejections() >= 2) {

            request.reject();

            return "ไม่อนุมัติคำขอ";
        }

        return "บันทึกความเห็นสำเร็จ";
    }

    public String cancelRequest(
            String requestId,
            String memberId) {

        RoleChangeRequest request =
                requestRepository.findById(requestId);

        if (request == null) {
            return "ปฏิเสธ: ไม่พบคำขอ";
        }

        if (!request.isPending()) {
            return "ปฏิเสธ: คำขอนี้ไม่อยู่ในสถานะรอพิจารณา";
        }

        if (!request.getRequester().getId().equals(memberId)) {
            return "ปฏิเสธ: เฉพาะผู้เสนอคำขอเท่านั้นที่ยกเลิกได้";
        }

        if (!request.getDecisions().isEmpty()) {
            return "ปฏิเสธ: มีสมาชิกลงความเห็นแล้ว ไม่สามารถยกเลิกได้";
        }

        request.cancel();

        return "ยกเลิกคำขอสำเร็จ";
    }
}