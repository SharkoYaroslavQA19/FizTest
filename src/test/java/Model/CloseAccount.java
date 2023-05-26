package Model;

public class CloseAccount {
    private Integer accountId;
    private Integer branchId;
    private String closeDate;

    public CloseAccount(Integer accountId, Integer branchId, String closeDate) {
        this.accountId = accountId;
        this.branchId = branchId;
        this.closeDate = closeDate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public String getCloseDate() {
        return closeDate;
    }
}
