package Model;

public class ClientDetails {

    private Integer clientId;
    private Integer branchId;
    private String clientKey;

    public ClientDetails(Integer clientId, Integer branchId, String clientKey){
        this.clientId=clientId;
        this.branchId =branchId;
        this.clientKey = clientKey;
    }



    public Integer getClientId() {
        return clientId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public String getClientKey() {
        return clientKey;
    }
}
