package com.navigation.service;


import com.navigation.entity.Manager;

public interface ManagerService {
    Manager managerlogin(String userName, String password);
}