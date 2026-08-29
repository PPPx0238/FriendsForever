package repository;

import model.Decision;

import java.util.ArrayList;
import java.util.List;

public class DecisionRepository {

    private List<Decision> decisions = new ArrayList<>();

    public void save(Decision decision) {
        decisions.add(decision);
    }

    public List<Decision> findAll() {
        return decisions;
    }
}