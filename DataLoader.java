package loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Decision;
import model.DecisionResult;
import model.Member;
import model.Role;
import model.RoleChangeRequest;
import repository.DecisionRepository;
import repository.MemberRepository;
import repository.RequestRepository;

public class DataLoader {

    public static void load(
            String filePath,
            MemberRepository memberRepository,
            RequestRepository requestRepository,
            DecisionRepository decisionRepository)
            throws IOException {

        String json = Files.readString(Path.of(filePath));

        loadMembers(json, memberRepository);

        loadRequests(json, memberRepository, requestRepository);

        loadDecisions(
                json,
                memberRepository,
                requestRepository,
                decisionRepository
        );
    }

    private static void loadMembers(
            String json,
            MemberRepository memberRepository) {

        String section =
                getSection(json, "members", "role_change_requests");

        Pattern pattern = Pattern.compile(
                "\\{\\s*"
                + "\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"name\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"role\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"active\"\\s*:\\s*(true|false)"
                + "\\s*\\}"
        );

        Matcher matcher = pattern.matcher(section);

        while (matcher.find()) {

            String id = matcher.group(1);
            String name = matcher.group(2);
            Role role = Role.valueOf(matcher.group(3));
            boolean active = Boolean.parseBoolean(
                    matcher.group(4)
            );

            Member member =
                    new Member(
                            id,
                            name,
                            role,
                            active
                    );

            memberRepository.save(member);
        }
    }

    private static void loadRequests(
            String json,
            MemberRepository memberRepository,
            RequestRepository requestRepository) {

        String section =
                getSection(
                        json,
                        "role_change_requests",
                        "decisions"
                );

        Pattern pattern = Pattern.compile(
                "\\{\\s*"
                + "\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"requester_id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"target_id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"new_role\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"status\"\\s*:\\s*\"([^\"]+)\""
                + "\\s*\\}"
        );

        Matcher matcher = pattern.matcher(section);

        while (matcher.find()) {

            String id = matcher.group(1);
            String requesterId = matcher.group(2);
            String targetId = matcher.group(3);
            Role newRole = Role.valueOf(matcher.group(4));
            String status = matcher.group(5);

            Member requester =
                    memberRepository.findById(requesterId);

            Member target =
                    memberRepository.findById(targetId);

            if (requester == null || target == null) {
                continue;
            }

            RoleChangeRequest request =
                    new RoleChangeRequest(
                            id,
                            requester,
                            target,
                            newRole
                    );

            switch (status) {

                case "APPROVED":
                    request.approve();
                    break;

                case "REJECTED":
                    request.reject();
                    break;

                case "CANCELLED":
                    request.cancel();
                    break;

                case "PENDING":
                    break;
            }

            requestRepository.save(request);
        }
    }

    private static void loadDecisions(
            String json,
            MemberRepository memberRepository,
            RequestRepository requestRepository,
            DecisionRepository decisionRepository) {

        String section =
                getSection(
                        json,
                        "decisions",
                        null
                );

        Pattern pattern = Pattern.compile(
                "\\{\\s*"
                + "\"request_id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"member_id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*"
                + "\"result\"\\s*:\\s*\"([^\"]+)\""
                + "\\s*\\}"
        );

        Matcher matcher = pattern.matcher(section);

        while (matcher.find()) {

            String requestId = matcher.group(1);
            String memberId = matcher.group(2);
            DecisionResult result =
                    DecisionResult.valueOf(matcher.group(3));

            RoleChangeRequest request =
                    requestRepository.findById(requestId);

            Member member =
                    memberRepository.findById(memberId);

            if (request == null || member == null) {
                continue;
            }

            Decision decision =
                    new Decision(
                            request,
                            member,
                            result
                    );

            request.addDecision(decision);

            decisionRepository.save(decision);
        }
    }

    private static String getSection(
            String json,
            String sectionName,
            String nextSectionName) {

        String startKey =
                "\"" + sectionName + "\"";

        int start = json.indexOf(startKey);

        if (start == -1) {
            return "";
        }

        start = json.indexOf("[", start);

        if (start == -1) {
            return "";
        }

        int end;

        if (nextSectionName != null) {

            int next =
                    json.indexOf(
                            "\"" + nextSectionName + "\"",
                            start
                    );

            end = json.lastIndexOf("]", next);

        } else {

            end = json.lastIndexOf("]");
        }

        if (end == -1) {
            return "";
        }

        return json.substring(
                start + 1,
                end
        );
    }
}