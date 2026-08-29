import loader.DataLoader;
import model.DecisionResult;
import model.Role;
import repository.DecisionRepository;
import repository.MemberRepository;
import repository.RequestRepository;
import service.RoleChangeService;

public class Main {

    public static void main(String[] args) throws Exception {

        MemberRepository memberRepository =
                new MemberRepository();

        RequestRepository requestRepository =
                new RequestRepository();

        DecisionRepository decisionRepository =
                new DecisionRepository();

        DataLoader.load(
                "seed_data.json",
                memberRepository,
                requestRepository,
                decisionRepository
        );

        RoleChangeService service =
                new RoleChangeService(
                        memberRepository,
                        requestRepository,
                        decisionRepository
                );

        System.out.println("=================================");
        System.out.println("FriendsForever - T1 to T6");
        System.out.println("=================================");

        System.out.println("\nT1: CREATE C05");

        String t1 = service.createRequest(
                "C05",
                "M05",
                "M01",
                Role.EDITOR
        );

        System.out.println("Result: " + t1);

        boolean t1Pass =
                requestRepository.findById("C05") != null
                && requestRepository.findById("C05").isPending();

        System.out.println("T1: " + (t1Pass ? "PASS" : "FAIL"));

        System.out.println("\nT2: CREATE C06");

        String t2 = service.createRequest(
                "C06",
                "M03",
                "M01",
                Role.CREATOR
        );

        System.out.println("Result: " + t2);

        boolean t2Pass =
                requestRepository.findById("C06") == null;

        System.out.println("T2: " + (t2Pass ? "PASS" : "FAIL"));

        System.out.println("\nT3: M04 APPROVE C01");

        String t3 = service.submitDecision(
                "C01",
                "M04",
                DecisionResult.APPROVE
        );

        System.out.println("Result: " + t3);

        boolean t3Pass =
                requestRepository.findById("C01").getStatus().name().equals("APPROVED")
                && memberRepository.findById("M02").getRole() == Role.EDITOR;

        System.out.println("T3: " + (t3Pass ? "PASS" : "FAIL"));

        System.out.println("\nT4: M05 REJECT C02");

        String t4 = service.submitDecision(
                "C02",
                "M05",
                DecisionResult.REJECT
        );

        System.out.println("Result: " + t4);

        boolean t4Pass =
                requestRepository.findById("C02").getStatus().name().equals("REJECTED")
                && memberRepository.findById("M03").getRole() == Role.EDITOR;

        System.out.println("T4: " + (t4Pass ? "PASS" : "FAIL"));

        System.out.println("\nT5: M03 CANCEL C03");

        String t5 = service.cancelRequest(
                "C03",
                "M03"
        );

        System.out.println("Result: " + t5);

        boolean t5Pass =
                requestRepository.findById("C03").getStatus().name().equals("CANCELLED");

        System.out.println("T5: " + (t5Pass ? "PASS" : "FAIL"));

        System.out.println("\nT6: M05 APPROVE C04");

        String t6 = service.submitDecision(
                "C04",
                "M05",
                DecisionResult.APPROVE
        );

        System.out.println("Result: " + t6);

        boolean t6Pass =
                requestRepository.findById("C04").isPending()
                && requestRepository.findById("C04").getDecisions().size() == 1;

        System.out.println("T6: " + (t6Pass ? "PASS" : "FAIL"));

        boolean allPass =
                t1Pass &&
                t2Pass &&
                t3Pass &&
                t4Pass &&
                t5Pass &&
                t6Pass;

        System.out.println("\n=================================");
        System.out.println("FINAL RESULT");
        System.out.println("=================================");
        System.out.println(
                allPass
                        ? "ALL TESTS PASSED"
                        : "SOME TESTS FAILED"
        );
    }
}