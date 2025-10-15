package com.tencentcloudapi.cls.plugin.network_diagnosis.netanalysis.bean;

public class CommandStatus {
    public static final CommandStatus CMD_STATUS_SUCCESSFUL = new CommandStatus("success");
    public static final CommandStatus CMD_STATUS_FAILED = new CommandStatus("failed");
    public static final CommandStatus CMD_STATUS_USER_STOP= new CommandStatus("user_stop");
    public static final CommandStatus CMD_STATUS_ERROR = new CommandStatus("error");
    public static final CommandStatus CMD_STATUS_NETWORK_ERROR = new CommandStatus("network_error");
    public static final CommandStatus CMD_STATUS_ERROR_UNKNOW_HOST = new CommandStatus("unkonown_host");
    String name;

    CommandStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

