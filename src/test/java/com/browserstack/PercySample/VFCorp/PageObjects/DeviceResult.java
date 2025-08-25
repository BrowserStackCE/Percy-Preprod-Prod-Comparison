package com.browserstack.PercySample.VFCorp.PageObjects;

import java.util.List;

public class DeviceResult {
    public String deviceName;
    public List<PageResult> pages;

    public DeviceResult(String deviceName, List<PageResult> pages) {
        this.deviceName = deviceName;
        this.pages = pages;
    }

    public List<PageResult> getPages()
    {
        return pages;
    }
}