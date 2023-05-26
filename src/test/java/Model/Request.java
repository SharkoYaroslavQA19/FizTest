package Model;

public class Request {
    private Integer clientId;
    private Integer branchId;

    public Request(Integer clientId, Integer branchId) {
        this.clientId = clientId;
        this.branchId = branchId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getBranchId() {
        return branchId;
    }
}