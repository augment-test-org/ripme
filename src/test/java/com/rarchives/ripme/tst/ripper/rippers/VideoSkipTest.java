package com.rarchives.ripme.tst.ripper.rippers;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.rarchives.ripme.ripper.AbstractRipper;
import com.rarchives.ripme.utils.Utils;

public class VideoSkipTest {

    @Test
    public void testVideoDetection() throws Exception {
        TestRipper ripper = new TestRipper(new URL("http://example.com"));
        
        // Test video URLs
        assertTrue(ripper.testIsVideoURL(new URL("http://example.com/video.mp4")));
        assertTrue(ripper.testIsVideoURL(new URL("http://example.com/video.webm")));
        assertTrue(ripper.testIsVideoURL(new URL("http://example.com/video.m4v")));
        assertTrue(ripper.testIsVideoURL(new URL("http://example.com/video.avi")));
        
        // Test non-video URLs
        assertFalse(ripper.testIsVideoURL(new URL("http://example.com/image.jpg")));
        assertFalse(ripper.testIsVideoURL(new URL("http://example.com/image.png")));
        assertFalse(ripper.testIsVideoURL(new URL("http://example.com/image.gif")));
    }

    @Test
    public void testVideoSkipping() throws Exception {
        TestRipper ripper = new TestRipper(new URL("http://example.com"));
        
        // Enable video skipping
        Utils.setConfigBoolean("download.skip_videos", true);
        
        // Should skip video URLs
        assertTrue(ripper.testShouldIgnoreURL(new URL("http://example.com/video.mp4")));
        assertTrue(ripper.testShouldIgnoreURL(new URL("http://example.com/video.webm")));
        
        // Should not skip non-video URLs
        assertFalse(ripper.testShouldIgnoreURL(new URL("http://example.com/image.jpg")));
        assertFalse(ripper.testShouldIgnoreURL(new URL("http://example.com/image.png")));
        
        // Disable video skipping
        Utils.setConfigBoolean("download.skip_videos", false);
        
        // Should not skip any URLs when disabled
        assertFalse(ripper.testShouldIgnoreURL(new URL("http://example.com/video.mp4")));
        assertFalse(ripper.testShouldIgnoreURL(new URL("http://example.com/image.jpg")));
    }

    // Test ripper class that exposes protected methods for testing
    private static class TestRipper extends AbstractRipper {
        public TestRipper(URL url) throws Exception {
            super(url);
        }

        public boolean testIsVideoURL(URL url) {
            return isVideoURL(url);
        }

        public boolean testShouldIgnoreURL(URL url) {
            return shouldIgnoreURL(url);
        }

        @Override
        public void rip() {}

        @Override
        public String getHost() {
            return "example.com";
        }

        @Override
        public String getGID(URL url) {
            return "test";
        }

        @Override
        public void setWorkingDir(URL url) {}

        @Override
        public String getAlbumTitle(URL url) {
            return "test";
        }

        @Override
        public boolean addURLToDownload(URL url, Path saveAs) {
            return false;
        }

        @Override
        public void downloadCompleted(URL url, Path saveAs) {}

        @Override
        public void downloadErrored(URL url, String reason) {}

        @Override
        public void downloadExists(URL url, Path file) {}

        @Override
        public int getCompletionPercentage() {
            return 0;
        }

        @Override
        public String getStatusText() {
            return "";
        }
    }
}
