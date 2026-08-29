package view;

import java.util.List;
import java.util.Scanner;

import controller.RoleChangeController;
import model.DecisionResult;
import model.Member;
import model.Role;
import model.RoleChangeRequest;

public class ConsoleView {

    private RoleChangeController controller;
    private Scanner scanner;

    public ConsoleView(RoleChangeController controller) {

        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        boolean running = true;

        while (running) {

            showMenu();

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    showMembers();
                    break;

                case "2":
                    createRequest();
                    break;

                case "3":
                    showRequests();
                    break;

                case "4":
                    submitDecision();
                    break;

                case "5":
                    cancelRequest();
                    break;

                case "0":
                    running = false;
                    System.out.println();
                    System.out.println("ออกจากโปรแกรม");
                    break;

                default:
                    System.out.println();
                    System.out.println("ปฏิเสธ: กรุณาเลือกเมนู 0-5");
            }
        }

        scanner.close();
    }

    private void showMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("          FRIENDS FOREVER");
        System.out.println("       ROLE CHANGE MANAGEMENT");
        System.out.println("========================================");
        System.out.println("1. แสดงรายการสมาชิก");
        System.out.println("2. สร้างคำขอเปลี่ยนบทบาท");
        System.out.println("3. แสดงรายการคำขอ");
        System.out.println("4. ลงความเห็นต่อคำขอ");
        System.out.println("5. ยกเลิกคำขอ");
        System.out.println("0. ออกจากโปรแกรม");
        System.out.println("========================================");
        System.out.print("เลือกเมนู: ");
    }

    private void showMembers() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("MEMBERS");
        System.out.println("========================================");

        List<Member> members =
                controller.getMembers();

        for (Member member : members) {

            System.out.println(
                    member.getId()
                    + " | "
                    + member.getName()
                    + " | Role: "
                    + member.getRole()
                    + " | Active: "
                    + member.isActive()
            );
        }
    }

    private void createRequest() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("CREATE ROLE CHANGE REQUEST");
        System.out.println("========================================");

        System.out.print("Request ID: ");
        String requestId = scanner.nextLine();

        System.out.print("Requester ID: ");
        String requesterId = scanner.nextLine();

        System.out.print("Target ID: ");
        String targetId = scanner.nextLine();

        System.out.print("New Role (PRODUCER / FINANCE / EDITOR / CREATOR): ");
        String roleInput = scanner.nextLine().toUpperCase();

        try {

            Role newRole =
                    Role.valueOf(roleInput);

            String result =
                    controller.createRequest(
                            requestId,
                            requesterId,
                            targetId,
                            newRole
                    );

            System.out.println();
            System.out.println(result);

        } catch (IllegalArgumentException e) {

            System.out.println();
            System.out.println("ปฏิเสธ: บทบาทไม่ถูกต้อง");
        }
    }

    private void showRequests() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("ROLE CHANGE REQUESTS");
        System.out.println("========================================");

        List<RoleChangeRequest> requests =
                controller.getRequests();

        for (RoleChangeRequest request : requests) {

            System.out.println(
                    request.getId()
                    + " | "
                    + request.getRequester().getId()
                    + " -> "
                    + request.getTarget().getId()
                    + " | New Role: "
                    + request.getNewRole()
                    + " | Status: "
                    + request.getStatus()
                    + " | Approve: "
                    + request.countApprovals()
                    + " | Reject: "
                    + request.countRejections()
            );
        }
    }

    private void submitDecision() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("SUBMIT DECISION");
        System.out.println("========================================");

        System.out.print("Request ID: ");
        String requestId = scanner.nextLine();

        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();

        System.out.print("Decision (APPROVE / REJECT): ");
        String resultInput =
                scanner.nextLine().toUpperCase();

        try {

            DecisionResult result =
                    DecisionResult.valueOf(resultInput);

            String message =
                    controller.submitDecision(
                            requestId,
                            memberId,
                            result
                    );

            System.out.println();
            System.out.println(message);

        } catch (IllegalArgumentException e) {

            System.out.println();
            System.out.println("ปฏิเสธ: ผลการลงความเห็นไม่ถูกต้อง");
        }
    }

    private void cancelRequest() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("CANCEL REQUEST");
        System.out.println("========================================");

        System.out.print("Request ID: ");
        String requestId = scanner.nextLine();

        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();

        String result =
                controller.cancelRequest(
                        requestId,
                        memberId
                );

        System.out.println();
        System.out.println(result);
    }
}