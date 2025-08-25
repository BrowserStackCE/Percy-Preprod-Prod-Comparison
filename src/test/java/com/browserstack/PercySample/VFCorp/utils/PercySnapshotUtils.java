package com.browserstack.PercySample.VFCorp.utils;

import browserstack.shaded.org.json.JSONObject;
import com.browserstack.PercySample.VFCorp.PageObjects.DeviceResult;
import com.browserstack.PercySample.VFCorp.PageObjects.PageResult;
import io.percy.selenium.Percy;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PercySnapshotUtils {


    private static ThreadLocal<List<PageResult>> summary = ThreadLocal.withInitial(ArrayList::new);
    private static ThreadLocal<String> deviceName = new ThreadLocal<>();


    public static void takeSapshot(Percy percy, String pageName, HashMap<String,Object> options)  {
        try {
            JSONObject data = percy.screenshot(pageName, options);
            System.out.println("Json data => " + data);
            PercyJsonParser parser = new PercyJsonParser(data);
            //summary.get().add(parser.extractData());
            addSummary(parser.extractData());
            //PercyReportGenerator.getInstance().setBuildNId(parser.getBuildId(data));
            if (deviceName == null) {
                deviceName.set(parser.getDeviceInfo());
            }
            System.out.println(data);
            System.out.println("Snapshot name => " + data.get("snapshot-name") + "; Status => " + data.get("status"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void finalizeSnapshots()
    {

        PercyReportGenerator.getInstance().addDeviceResult(new DeviceResult(deviceName.get(), getSummary()));
    }

    public static void addSummary(PageResult record) {
        summary.get().add(record);
    }

    public static List<PageResult> getSummary() {
        return summary.get();
    }
}
