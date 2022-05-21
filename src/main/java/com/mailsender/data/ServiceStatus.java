package com.mailsender.data;

public class ServiceStatus {

    private boolean isWorking;
    private ServiceStatus instance;

    public static class ServiceStatusHolder {
        public static ServiceStatus serviceStatusInstance = new ServiceStatus();
    }

    public static ServiceStatus getInstance(){
        return ServiceStatusHolder.serviceStatusInstance;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
