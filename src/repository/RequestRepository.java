package repository;

import model.RequestStatus;
import model.RoleChangeRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository {

    private List<RoleChangeRequest> requests = new ArrayList<>();

    public void save(RoleChangeRequest request) {
        requests.add(request);
    }

    public RoleChangeRequest findById(String id) {

        for (RoleChangeRequest request : requests) {

            if (request.getId().equals(id)) {
                return request;
            }
        }

        return null;
    }

    public RoleChangeRequest findPendingByTarget(String targetId) {

        for (RoleChangeRequest request : requests) {

            if (request.getTarget().getId().equals(targetId)
                    && request.getStatus() == RequestStatus.PENDING) {

                return request;
            }
        }

        return null;
    }

    public List<RoleChangeRequest> findAll() {
        return requests;
    }
}