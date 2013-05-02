package cz.muni.fi.pv243.et.model;

import java.util.Date;
import java.util.List;

public class ExpenseReport {

    private Long id;
    private Person submitter;
    private Person verifier;
    private List<Payment> payments;
    private List<MoneyTransfer> moneyTransfers;
    private Date lastSubmittedDate;
    private Date approvedDate;
    private ReportStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getSubmitter() {
        return submitter;
    }

    public void setSubmitter(Person submitter) {
        this.submitter = submitter;
    }

    public Person getVerifier() {
        return verifier;
    }

    public void setVerifier(Person verifier) {
        this.verifier = verifier;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<MoneyTransfer> getMoneyTransfers() {
        return moneyTransfers;
    }

    public void setMoneyTransfers(List<MoneyTransfer> moneyTransfers) {
        this.moneyTransfers = moneyTransfers;
    }

    public Date getLastSubmittedDate() {
        return lastSubmittedDate;
    }

    public void setLastSubmittedDate(Date lastSubmittedDate) {
        this.lastSubmittedDate = lastSubmittedDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseReport that = (ExpenseReport) o;

        if (approvedDate != null ? !approvedDate.equals(that.approvedDate) : that.approvedDate != null) return false;
        if (!id.equals(that.id)) return false;
        if (!lastSubmittedDate.equals(that.lastSubmittedDate)) return false;
        if (moneyTransfers != null ? !moneyTransfers.equals(that.moneyTransfers) : that.moneyTransfers != null)
            return false;
        if (payments != null ? !payments.equals(that.payments) : that.payments != null) return false;
        if (status != that.status) return false;
        if (!submitter.equals(that.submitter)) return false;
        if (verifier != null ? !verifier.equals(that.verifier) : that.verifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + submitter.hashCode();
        result = 31 * result + (verifier != null ? verifier.hashCode() : 0);
        result = 31 * result + (payments != null ? payments.hashCode() : 0);
        result = 31 * result + (moneyTransfers != null ? moneyTransfers.hashCode() : 0);
        result = 31 * result + lastSubmittedDate.hashCode();
        result = 31 * result + (approvedDate != null ? approvedDate.hashCode() : 0);
        result = 31 * result + status.hashCode();
        return result;
    }
}