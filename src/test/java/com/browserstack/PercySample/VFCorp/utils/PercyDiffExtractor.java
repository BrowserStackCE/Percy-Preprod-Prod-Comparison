package com.browserstack.PercySample.VFCorp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
public class PercyDiffExtractor {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("snapshots-1.json"));

        JsonNode included = root.get("included");
        if (included == null || !included.isArray()) {
            System.out.println("No included section found.");
            return;
        }

        // Map comparison-tag ID -> tag attributes
        Map<String, JsonNode> tagMap = new HashMap<>();
        // Map snapshot ID -> snapshot name
        Map<String, String> snapshotMap = new HashMap<>();

        for (JsonNode item : included) {
            String type = item.path("type").asText();
            if ("comparison-tags".equals(type)) {
                tagMap.put(item.path("id").asText(), item.path("attributes"));
            } else if ("snapshots".equals(type)) {
                snapshotMap.put(item.path("id").asText(), item.path("attributes").path("name").asText());
            }
        }

        // Iterate comparisons
        for (JsonNode item : included) {
            if ("comparisons".equals(item.path("type").asText())) {
                double diffRatio = item.path("attributes").path("diff-ratio").isNull()
                        ? -1
                        : item.path("attributes").path("diff-ratio").asDouble();

                // Get comparison-tag ID
                JsonNode tagNode = item.path("relationships").path("comparison-tag").path("data");
                String tagId = tagNode.isMissingNode() || tagNode.isNull() ? null : tagNode.path("id").asText();

                // Get head snapshot ID from data or from link
                String headSnapshotId = null;
                JsonNode headSnapshotData = item.path("relationships").path("head-snapshot").path("data");
                if (!headSnapshotData.isMissingNode() && !headSnapshotData.isNull()) {
                    headSnapshotId = headSnapshotData.path("id").asText();
                } else {
                    String relatedUrl = item.path("relationships").path("head-snapshot").path("links").path("related").asText();
                    if (relatedUrl != null && relatedUrl.contains("/")) {
                        headSnapshotId = relatedUrl.substring(relatedUrl.lastIndexOf("/") + 1);
                    }
                }

                String snapshotName = snapshotMap.getOrDefault(headSnapshotId, "Unknown Snapshot");

                String browserName = null, browserVersion = null, osName = null, osVersion = null;
                if (tagId != null && tagMap.containsKey(tagId)) {
                    JsonNode tagAttr = tagMap.get(tagId);
                    browserName = tagAttr.path("browser-name").asText();
                    browserVersion = tagAttr.path("browser-version").asText();
                    osName = tagAttr.path("os-name").asText();
                    osVersion = tagAttr.path("os-version").asText();
                }

                System.out.printf(
                        "Snapshot: %s | Diff Ratio: %s | Browser: %s %s | OS: %s %s%n",
                        snapshotName,
                        diffRatio == -1 ? "null" : diffRatio,
                        browserName, browserVersion, osName, osVersion
                );
            }
        }
    }
}


