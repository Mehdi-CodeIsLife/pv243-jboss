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

    @Produces
    public Collection<ExpenseReport> getAllforVerifier() {
        return service.findForVerifier(currentPerson.getPerson());
    }

    @Produces
    public Collection<ExpenseReport> getAllforSubmitter() {
        return service.findForSubmitter(currentPerson.getPerson());
    }

    @Produces
    public Collection<ExpenseReport> getAllWithNoVerifierAssigned() {
        List<ExpenseReport> reports = (List<ExpenseReport>) service.findWithNoVerifierAssigned();
        model.setReports(reports);
        return reports;
    }

    public String showSingleReport(Long id) {
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
        List<ExpenseReport> selected = model.getReports();
        for (ExpenseReport er : selected) {
            if (er.getSelected()) {
                logger.debug("Report selected=" + er.getName());
                service.claim(er, currentPerson.getPerson());
            } else {
                System.out.println("Report NOT selected=" + er.getName());
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
        return !(status == ReportStatus.SUBMITTED || status == ReportStatus.APPROVED || status == ReportStatus.SETTLED );
    }

    public boolean isSubmitted() {
        ReportStatus status = model.getReport().getStatus();
        return (status == ReportStatus.SUBMITTED);
    }

    public String rejectReport() {
        service.setStatus(model.getReport(), ReportStatus.REJECTED);
        return "/secured/reports";
    }

    public String approveReport() {
        service.setStatus(model.getReport(), ReportStatus.APPROVED);
        return "/secured/reports";
    }

}
