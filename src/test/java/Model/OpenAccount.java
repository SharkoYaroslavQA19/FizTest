package Model;

public class OpenAccount {

    private Integer clientId;
    private Integer branchId;
    private Integer planId;
    private Integer currencyId;
    private Integer number;

    public OpenAccount(Integer clientId, Integer branchId, Integer planId, Integer currencyId, Integer number) {
        this.clientId = clientId;
        this.branchId = branchId;
        this.planId = planId;
        this.currencyId = currencyId;
        this.number = number;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public Integer getNumber() {
        return number;
    }
}
