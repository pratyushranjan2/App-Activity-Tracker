package com.example.appusage;

public class AppInfo {

    private String name;
    private String packageName;
    private int imageId;
    private String date;
    private String usage;

    AppInfo(String name, String packageName, int imageId, String date) {
        this.name = name;
        this.imageId = imageId;
        this.packageName = packageName;
        this.date = date;
    }

    String getName() {
        return name;
    }

    String getPackageName() {
        return packageName;
    }

    int getImageId() {
        return imageId;
    }

    String getDate() {
        return date;
    }

    String getUsage() {
        return usage;
    }

    void setUsage(String usage) {
        this.usage = usage;
    }
}
