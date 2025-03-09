package com.rarchives.ripme.ripper;

import com.rarchives.ripme.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StopOnFirstDuplicateTest {

    @Test
    public void testStopOnFirstDuplicate() throws IOException, URISyntaxException {
        // Enable stop on first duplicate
        Utils.setConfigBoolean("download.stop_on_first_duplicate", true);

        // Create a test ripper that simulates duplicate files
        TestDuplicateRipper ripper = new TestDuplicateRipper(new URL("http://test.com"));
        ripper.setup();
        ripper.rip();

        // Should only download 1 file and stop on the second (duplicate)
        assertEquals(1, ripper.getCompletedDownloads());

        // Reset config
        Utils.setConfigBoolean("download.stop_on_first_duplicate", false);
    }

    private static class TestDuplicateRipper extends AbstractJSONRipper {
        private int completedDownloads = 0;

        public TestDuplicateRipper(URL url) throws IOException {
            super(url);
        }

        @Override
        protected String getDomain() {
            return "test.com";
        }

        @Override
        public String getHost() {
            return "test";
        }

        @Override
        public String getGID(URL url) throws MalformedURLException {
            return "test";
        }

        @Override
        protected JSONObject getFirstPage() throws IOException {
            JSONObject json = new JSONObject();
            JSONArray urls = new JSONArray();
            urls.put("http://test.com/1.jpg");
            urls.put("http://test.com/1.jpg");
            json.put("urls", urls);
            return json;
        }

        @Override
        protected JSONObject getNextPage(JSONObject json) throws IOException {
            return null;
        }

        @Override
        protected List<String> getURLsFromJSON(JSONObject json) {
            List<String> urls = new ArrayList<>();
            JSONArray urlArray = json.getJSONArray("urls");
            for (int i = 0; i < urlArray.length(); i++) {
                urls.add(urlArray.getString(i));
            }
            return urls;
        }

        @Override
        protected void downloadURL(URL url, int index) {
            // Simulate download
            completedDownloads++;
        }

        public int getCompletedDownloads() {
            return completedDownloads;
        }
    }
}
