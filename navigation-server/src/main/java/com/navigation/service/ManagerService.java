package com.navigation.service;

import com.navigation.entity.Manager;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService {
    Manager managerlogin(String userName, String password);
}
