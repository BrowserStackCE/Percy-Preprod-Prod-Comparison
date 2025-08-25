package com.browserstack.PercySample.VFCorp.PageObjects;

public class PageResult {
    public String pageName;
    public String status;  // "Pass" or "Fail"
    public double diffRatio; // percentage

    public PageResult(String pageName, String status, double diffRatio) {
        this.pageName = pageName;
        this.status = status;
        this.diffRatio = diffRatio;
    }
}