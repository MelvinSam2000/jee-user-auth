package com.msam.jee.ejb.user;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Singleton
public class RawPassUserRepo implements UserRepo, RawPassUserRepoMBean {

    final Map<String, String> usersMap = new HashMap<>();

    ObjectName jmxObjName;

    @PostConstruct
    public void registerMBean() {
        try {
            String packageStr = RawPassUserRepo.class.getPackageName();
            String classStr = RawPassUserRepo.class.getSimpleName();
            jmxObjName = new ObjectName(String.format("%s:type=%s", packageStr, classStr));
            ManagementFactory.getPlatformMBeanServer().registerMBean(this, jmxObjName);
            log.info("MBean registered [{}]", jmxObjName.toString());
        } catch (MalformedObjectNameException |
                 InstanceAlreadyExistsException |
                 MBeanRegistrationException |
                 NotCompliantMBeanException e) {
            log.error("Could not register MBean for RawPassUserRepo", e);
        }
    }

    @PreDestroy
    public void unregisterMBean() {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(jmxObjName);
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            log.error("Could not unregister MBean for RawPassUserRepo", e);
        }
    }

    @Override
    @Lock(LockType.READ)
    public boolean validateCredentials(String username, String rawPassword) {
        if (!usersMap.containsKey(username)) {
            return false;
        }
        return usersMap.get(username).equals(rawPassword);
    }

    @Override
    @Lock(LockType.WRITE)
    public void createUser(String username, String rawPassword) {
        if (usersMap.containsKey(username)) {
            return;
        }
        usersMap.put(username, rawPassword);
    }

    @Override
    @Lock(LockType.WRITE)
    public boolean deleteUser(String username) {
        if (!usersMap.containsKey(username)) {
            return false;
        }
        usersMap.remove(username);
        return true;
    }

    @Override
    @Lock(LockType.READ)
    public List<String> getUsernames() {
        return usersMap.keySet().stream().toList();
    }

    @Override
    public Map<String, String> getUsers() {
        return usersMap;
    }
}
