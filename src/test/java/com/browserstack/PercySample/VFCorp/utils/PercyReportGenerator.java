package com.browserstack.PercySample.VFCorp.utils;

import com.browserstack.PercySample.VFCorp.PageObjects.DeviceResult;
import com.browserstack.PercySample.VFCorp.PageObjects.PageResult;
import de.vandermeer.asciitable.AsciiTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PercyReportGenerator {


    public  String buildId;
    public List<PageResult> summary;
    public List<DeviceResult> details;

    public List<PageResult> getSummary() {
        return summary;
    }

    public List<DeviceResult> getDetails() {
        return details;
    }

    public static PercyReportGenerator instance=null;

    /*public static void main(String[] args) {
        // ====== SAMPLE DATA ======
        List<PageResult> summary = Arrays.asList(
                new PageResult("Homepage", "Pass", 0.0),
                new PageResult("Login Page", "Fail", 12.45)
        );

        List<DeviceResult> details = Arrays.asList(
                new DeviceResult("iPhone 14 Pro", Arrays.asList(
                        new PageResult("Homepage", "Pass", 0.0),
                        new PageResult("Login Page", "Fail", 12.45)
                )),
                new DeviceResult("Samsung Galaxy S22", Arrays.asList(
                        new PageResult("Homepage", "Pass", 0.0),
                        new PageResult("Login Page", "Fail", 8.13)
                ))
        );

    }*/

    private PercyReportGenerator()
    {
        summary = new ArrayList<>();
        details = new ArrayList<>();
    }

    public static PercyReportGenerator getInstance()
    {
        if(instance==null)
        {
            instance =  new PercyReportGenerator();
        }
        return instance;
    }

    public void addPageResult(PageResult pageData)
    {
        summary.add(pageData);
    }

    public void addDeviceResult(DeviceResult deviceData)
    {
        details.add(deviceData);
    }

    public void summarize()
    {
        System.out.println("Summarize method called");
        HashMap<String, String> results = new HashMap<>();
            for(DeviceResult device: details)
            {
                for(PageResult page: device.getPages())
                {
                    if(results.get(page.pageName) ==null)
                    {
                        results.put(page.pageName, page.status);
                    }else if(!results.get(page.pageName).equalsIgnoreCase("fail"))
                    {
                        results.put(page.pageName, page.status);
                    }

                }
            }
            for(Map.Entry<String,String> item: results.entrySet())
            {
                summary.add(new PageResult(item.getKey(), item.getValue(), 0.0));
            }

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("KEY", "VALUE");
        at.addRule();

        results.forEach((k, v) -> {
            at.addRow(k, v);
            at.addRule();
        });

        System.out.println(at.render());

        System.out.println("Summarize method call ended");
    }


    public void setBuildNId(String buildId)
    {
        this.buildId=buildId;
    }



    public void generateReport()
    {
        //System.out.println("generateReport method called for Build " +buildId);


        //System.out.println("BUILD_ID=" + this.buildId);


        // ====== GENERATE REPORT ======
        String html = generateHtml(summary, details);

        System.out.println("Summary report size =>"+summary.size()+"; Details report size =>"+details.size());

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("S.NO.", "Page Name", "Status");
        at.addRule();

        for (int i = 0; i < summary.size(); i++) {
            at.addRow(i + 1, summary.get(i).pageName, summary.get(i).status);
            at.addRule();
        }

        System.out.println(at.render());


        /*for (int i = 0; i < details.size(); i++) {
            at.addRow(i + 1, details.get(i).pages,details.get(i).deviceName);
            at.addRule();
        }

        System.out.println(at.render());*/

        // ====== WRITE TO FILE ======
        try (FileWriter writer = new FileWriter("percy-report.html")) {
            writer.write(html);
            System.out.println("Percy report generated: percy-report.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("generateReport method call ended");
    }

    private static String generateHtml(List<PageResult> summary, List<DeviceResult> details) {
        System.out.println("generateHtml method called");

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html lang=\"en\"><head>")
                .append("<meta charset=\"UTF-8\" />")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>")
                .append("<title>Percy Build Report</title>")
                .append("<style>")
                .append("body{font-family:Arial,sans-serif;margin:20px;background-color:#f9f9f9;}")
                .append("h1,h2{color:#333;}")
                .append("table{width:100%;border-collapse:collapse;margin-bottom:30px;}")
                .append("th,td{border:1px solid #ddd;padding:10px;text-align:left;}")
                .append("th{background-color:#f2f2f2;}")
                .append(".status-pass{color:green;font-weight:bold;}")
                .append(".status-fail{color:red;font-weight:bold;}")
                .append(".device-section{margin-bottom:40px;}")
                .append(".device-header{background-color:#eaeaea;padding:8px;font-weight:bold;}")
                .append("</style></head><body>")
                .append("<h1>Percy Build Report</h1>");

        // Summary section
        sb.append("<h2>Summary</h2>")
                .append("<table class=\"summary-table\"><thead><tr><th>Page Name</th><th>Status</th></tr></thead><tbody>");
        for (PageResult pr : summary) {
            sb.append("<tr><td>").append(pr.pageName).append("</td>")
                    .append("<td class=\"status-").append(pr.status.toLowerCase()).append("\">")
                    .append(pr.status).append("</td></tr>");
        }
        sb.append("</tbody></table>");

        // Details section
        sb.append("<h2>Details</h2>");
        for (DeviceResult dr : details) {
            sb.append("<div class=\"device-section\">")
                    .append("<div class=\"device-header\">Device: ").append(dr.deviceName).append("</div>")
                    .append("<table class=\"details-table\"><thead><tr><th>Page</th><th>Status</th><th>Diff Ratio (%)</th></tr></thead><tbody>");
            for (PageResult pr : dr.pages) {
                sb.append("<tr><td>").append(pr.pageName).append("</td>")
                        .append("<td class=\"status-").append(pr.status.toLowerCase()).append("\">")
                        .append(pr.status).append("</td>")
                        .append("<td>").append(String.format("%.2f", pr.diffRatio)).append("</td></tr>");
            }
            sb.append("</tbody></table></div>");
        }

        sb.append("</body></html>");

        System.out.println("generateHtml method call ended");

        return sb.toString();



    }
}
