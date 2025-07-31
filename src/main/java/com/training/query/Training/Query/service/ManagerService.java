package com.training.query.Training.Query.service;

import com.training.query.Training.Query.collections.Manager;
import com.training.query.Training.Query.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Manager getManagerByEmployeeId(String employeeId) {
        return managerRepository.findByEmployeeIdsContains(employeeId);
    }

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }
    public Manager getManagerById(String id) {
        return managerRepository.findById(id).orElse(null);
    }
}

