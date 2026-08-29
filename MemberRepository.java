package repository;

import model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    private List<Member> members = new ArrayList<>();

    public void save(Member member) {
        members.add(member);
    }

    public Member findById(String id) {

        for (Member member : members) {

            if (member.getId().equals(id)) {
                return member;
            }
        }

        return null;
    }

    public List<Member> findAll() {
        return members;
    }
}