package com.demo.config;

import org.springframework.batch.item.ItemProcessor;

import com.demo.model.Loan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class UserItemProcessor implements ItemProcessor<Loan, Loan> {

    private final List<SimpleDateFormat> inputDateFormats = Arrays.asList(
            new SimpleDateFormat("MM/dd/yyyy"),
            new SimpleDateFormat("MM-dd-yyyy"),
            new SimpleDateFormat("yyyy-MM-dd")
    );
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Loan process(Loan loan) throws Exception {
        loan.setEffective_date(formatDate(loan.getEffective_date()));
        loan.setDue_date(formatDate(loan.getDue_date()));
        loan.setPaid_off_time(formatDate(loan.getPaid_off_time()));
        return loan;
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        Date date = null;
        for (SimpleDateFormat format : inputDateFormats) {
            try {
                date = format.parse(dateString);
                break;
            } catch (ParseException e) {
            }
        }

        if (date == null) {
            return null;
        }

        return outputDateFormat.format(date);
    }
}