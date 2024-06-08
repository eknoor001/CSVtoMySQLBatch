package com.demo.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    private String loan_id;
    private String status;
    private int principal;
    private int terms;
    private String effective_date;
    private String due_date;
    private String paid_off_time;
    private int past_due_days;
    private int age;
    private String education;
    private String gender;

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrincipal() {
        return principal;
    }

    public void setPrincipal(int principal) {
        this.principal = principal;
    }

    public int getTerms() {
        return terms;
    }

    public void setTerms(int terms) {
        this.terms = terms;
    }

    public String getEffective_date() {
        return effective_date;
    }

    public void setEffective_date(String effective_date) {
        this.effective_date = effective_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getPaid_off_time() {
        return paid_off_time;
    }

    public void setPaid_off_time(String paid_off_time) {
        this.paid_off_time = paid_off_time;
    }

    public int getPast_due_days() {
        return past_due_days;
    }

    public void setPast_due_days(int past_due_days) {
        this.past_due_days = past_due_days;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

