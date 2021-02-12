package main.java.listeners;

import main.java.reports.ExtentManager;
import main.java.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static main.java.constants.GlobalData.FILE_NAME_REPORT;
import static main.java.constants.GlobalData.OUTPUT_FOLDER_REPORT;

public class SuiteListener implements ISuiteListener {
    public static String componentName;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SuiteListener() {
    }

    public void onStart(ISuite iSuite) {
        this.createNewExtentFolder();
        componentName = iSuite.getName().toUpperCase();
        try {
            PropertyUtil.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onFinish(ISuite iSuite) {
        ExtentManager.createInstance();
        ExtentManager.addExecutionDetails_ExtentReport();
        if (!iSuite.getName().contains("Cucumber"))
            logger.info("Report saved at path: {}", OUTPUT_FOLDER_REPORT + FILE_NAME_REPORT);
    }

    private void createNewExtentFolder() {
        Path extentFolderPath = Paths.get("report");
        try {
            if (Files.exists(extentFolderPath)) {
                deleteDir(extentFolderPath.toFile());
            }
            Files.createDirectory(extentFolderPath);

        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
