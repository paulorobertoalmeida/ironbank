package com.ihbank.ihbank.services;


import com.ihbank.ihbank.models.accounts.StudentChecking;
import com.ihbank.ihbank.repositories.accounts.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCheckingService {


    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    public List<StudentChecking> allStudentAccounts() {
        return studentCheckingRepository.findAll();
    }
}