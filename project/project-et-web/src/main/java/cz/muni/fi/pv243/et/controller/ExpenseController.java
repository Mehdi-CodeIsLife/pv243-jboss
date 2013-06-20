package cz.muni.fi.pv243.et.controller;

import cz.muni.fi.pv243.et.model.PersonWrapper;
import cz.muni.fi.pv243.et.model.*;
import cz.muni.fi.pv243.et.service.ExpenseReportService;
import cz.muni.fi.pv243.et.util.CurrentPerson;
import org.jboss.solder.logging.Log;
import org.jboss.solder.logging.Logger;


import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Model
public class ExpenseController {

    @Inject
    private ExpenseModel model;

    @Inject
    private ExpenseReportService service;

    @Inject
    @CurrentPerson
    private PersonWrapper currentPerson;

    @Inject
    Logger logger;

    private List<ExpenseReport> selectedReports;


    public List<ExpenseReport> getSelectedReports() {
        return selectedReports;
    }

    public void setSelectedReports(List<ExpenseReport> selectedReports) {
        this.selectedReports = selectedReports;
    }

    @Produces
    @Named("expenseReports")
    public Collection<ExpenseReport> getAllReports() {
        return service.findAll();
    }

    public Collection<ExpenseReport> allOpenForVerifier() {
        return service.findForVerifierWithStatus(currentPerson.getPerson(), ReportStatus.OPEN);
    }

    public Collection<ExpenseReport> getAllforSubmitter() {
        return service.findForSubmitter(currentPerson.getPerson());
    }

    public Collection<ExpenseReport> getAllWithNoVerifierAssigned() {
        List<ExpenseReport> reports = (List<ExpenseReport>) service.findWithNoVerifierAssigned();
        model.setReports(reports);
        return reports;
    }

    public String showSingleReport(Long id, String fromUrl) {
        model.setBackUrl(fromUrl);
        model.setReport(service.get(id));

        return "/secured/report";
    }

    public String editReport(Long id) {
        model.setReport(service.get(id));

        return "/secured/editReport";
    }

    public String createReport() {
        model.setReport(new ExpenseReport());

        return "/secured/createReport";
    }

    public String saveReport() {
        ExpenseReport r = model.getReport();
        boolean isNew = r.getId() == null;

        r.setLastChangeDate(new Date());
        if (isNew) {
            r.setStatus(ReportStatus.OPEN);
            r.setSubmitter(currentPerson.getPerson());
            r.setMoneyTransfers(new ArrayList<MoneyTransfer>());
            r.setPayments(new ArrayList<Payment>());
            r.setLastSubmittedDate(new Date());
        }
        service.save(r);

        model.setReport(r);

        return "/secured/report?faces-redirect=true";
    }

    public String claimReports() {
        logger.debug("\n=======\n" + model.getReports().get(0) + "\n=======\n");
        List<ExpenseReport> selected = model.getReports();
        for (ExpenseReport er : selected) {
            if (er.getSelected()) {
                logger.debug("Report selected=" + er.getName());
                service.claim(er, currentPerson.getPerson());
            } else {
                logger.debug("Report NOT selected=" + er.getName());
            }
        }
        return "/secured/reports";
    }

    public String submitReport() {
        service.submit(model.getReport());
        return "/secured/reports";
    }

    public boolean isSubmittable() {
        ReportStatus status = model.getReport().getStatus();
        boolean submittableByStatus = !(status == ReportStatus.SUBMITTED || status == ReportStatus.APPROVED || status == ReportStatus.SETTLED);
        boolean submittableByPayment = !(model.getReport().getPayments().isEmpty());

        return (submittableByPayment && submittableByStatus);
    }

    public boolean isSubmitted() {
        ReportStatus status = model.getReport().getStatus();
        return (status == ReportStatus.SUBMITTED);
    }

    public boolean isApproved() {
        ReportStatus status = model.getReport().getStatus();
        return (status == ReportStatus.APPROVED);
    }

    public String rejectReport() {
        service.setStatus(model.getReport(), ReportStatus.REJECTED);
        return "/secured/report";
    }

    public String approveReport() {
        service.approve(model.getReport());
        return "/secured/report";
    }

    public String sendMoney() {
        service.setStatus(model.getReport(), ReportStatus.SETTLED);
        return "/secured/report";
    }

    public String cancel() {
        return model.getBackUrl();
    }

}
