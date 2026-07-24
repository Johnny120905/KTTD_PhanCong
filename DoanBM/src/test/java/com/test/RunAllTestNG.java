package com.test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class RunAllTestNG {
      public static void main(String[] args) {

        System.out.println("===== START RUN TESTNG.XML =====");

        File suiteFile = new File("testng.xml");

        if (!suiteFile.exists()) {
            System.out.println("Không tìm thấy testng.xml tại: " + suiteFile.getAbsolutePath());
            System.out.println("Hãy mở đúng folder DoanBM trong VS Code rồi chạy lại.");
            return;
        }

        List<String> suites = new ArrayList<>();
        suites.add(suiteFile.getAbsolutePath());

        TestNG testng = new TestNG();
        testng.setTestSuites(suites);
        testng.setUseDefaultListeners(true);

        testng.run();

        if (testng.hasFailure()) {
            System.out.println("===== TEST SUITE CÓ TEST FAIL =====");
            System.exit(1);
        } else {
            System.out.println("===== TEST SUITE PASS HẾT =====");
        }
    }
}
