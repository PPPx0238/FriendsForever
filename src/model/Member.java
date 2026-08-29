package model;

public class Member {

    private String id;
    private String name;
    private Role role;
    private boolean active;

    public Member(String id, String name, Role role, boolean active) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }
}