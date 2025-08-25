package com.browserstack.PercySample.VFCorp.utils;

import browserstack.shaded.org.json.JSONArray;
import browserstack.shaded.org.json.JSONObject;
import com.browserstack.PercySample.VFCorp.PageObjects.PageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PercyJsonParser {

    String json = "{\n" +
            "  \"snapshot-name\":\"Home Page\",\n" +
            "  \"started-processing-at\":\"2025-08-17T15:02:35.000Z\",\n" +
            "  \"finished-processing-at\":\"2025-08-17T15:02:36.000Z\",\n" +
            "  \"dashboard-urls\":{\n" +
            "    \"base-snapshot\":null,\n" +
            "    \"current-snapshot\":\"https://percy.io/385b3df5/VFC-Sample-1-ae09ae2b/snapshot/2260797223/default-comparison\",\n" +
            "    \"current-build\":\"https://percy.io/385b3df5/web/VFC-Sample-1-ae09ae2b/builds/42314706\",\n" +
            "    \"base-build\":null\n" +
            "  },\n" +
            "  \"screenshots\":[{\n" +
            "    \"started-processing-at\":\"2025-08-17T15:02:35.000Z\",\n" +
            "    \"viewport\":{\"width\":1296,\"height\":944},\n" +
            "    \"finished-processing-at\":\"2025-08-17T15:02:36.000Z\",\n" +
            "    \"resources\":{\"intelli-ignore-diff-image\":null,\"base-image\":null,\"current-image\":\"https://images.percy.io/191bf6f1f67795d12ad9d3ba1e2aa318575175c4cf4a55d2fcd629906798b9b3\",\"diff-image\":null},\n" +
            "    \"diff-info\":{\"diff-ratio\":null,\"diff-coordinates\":null},\n" +
            "    \"platform-information\":{\"browser-info\":{\"name\":\"firefox\",\"version\":\"141\"},\"device-info\":{\"name\":\"Windows_10_firefox_141\"},\"os-info\":{\"name\":\"Windows\",\"version\":\"10\"}}\n" +
            "  }],\n" +
            "  \"status\":\"success\"\n" +
            "}";

    JSONObject snapshotJson;
    ThreadLocal<String> deviceName = new ThreadLocal<>();

    public static void main(String[] args) {

    }

    public PercyJsonParser(JSONObject screenshotDetails)
    {
         //json = screenshotDetails;
        snapshotJson = screenshotDetails;
    }

    public PageResult extractData()
    {
        JSONObject root;
        try {
            if(snapshotJson==null) {
                 root = new JSONObject(json);
            }
            else
                {
                    root = snapshotJson;
                }

            // snapshot-name
            String snapshotName = root.getString("snapshot-name");

            // get first screenshot
            JSONArray screenshots = root.getJSONArray("screenshots");
            JSONObject firstScreenshot = screenshots.getJSONObject(0);

            // diff-ratio
            Object diffRatioObj = firstScreenshot.getJSONObject("diff-info").opt("diff-ratio");
            String diffRatio = (diffRatioObj == JSONObject.NULL) ? null : diffRatioObj.toString();

            // device-info
            String deviceInfo = firstScreenshot
                    .getJSONObject("platform-information")
                    .getJSONObject("device-info")
                    .getString("name");

            System.out.println("Snapshot Name: " + snapshotName);
            System.out.println("Diff Ratio: " + diffRatio);
            System.out.println("Device Info: " + deviceInfo);
            deviceName.set(deviceInfo);
            String currentBuildUrl = root.getJSONObject("dashboard-urls").getString("current-build");
            String buildId=getBuildId(currentBuildUrl);
            try {
                FileWriter writer = new FileWriter("output.txt");
                writer.write(buildId);
                writer.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            if(diffRatio==null)
            {
                return new PageResult(snapshotName, "pass", 0.0);
            }else if(Double.valueOf(diffRatio)*100>20.0)
            {
                return new PageResult(snapshotName, "fail", Double.valueOf(diffRatio)*100);
            }else
            {
                return new PageResult(snapshotName, "pass", Double.valueOf(diffRatio)*100);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeviceInfo()
    {
        return deviceName.get();
    }

    public String getBuildId(String  currentBuildUrl)
    {
        //JSONObject obj = new JSONObject(JSONObject);
        //System.out.println("Snapshot json from getBuildId => "+snapshotJson);
        //String currentBuildUrl = obj.getJSONObject("dashboard-urls").getString("current-build");

        System.out.println("currentBuildUrl => "+currentBuildUrl);
        // Extract build id using regex
        Pattern pattern = Pattern.compile(".*/builds/(\\d+)$");
        Matcher matcher = pattern.matcher(currentBuildUrl);

        if (matcher.find()) {
            String buildId = matcher.group(1);
            System.out.println("Build ID: " + buildId);
            return buildId;
        } else {
            System.out.println("Build ID not found.");
            return null;
        }

    }
}
