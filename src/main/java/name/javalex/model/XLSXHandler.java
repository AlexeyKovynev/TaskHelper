package name.javalex.model;

import name.javalex.entities.SimplifiedProcess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class XLSXHandler {

    // Make a single instance to prevent multiple write access to the file
    private static final XLSXHandler INSTANCE = new XLSXHandler();
    public static XLSXHandler getInstance() {
        return INSTANCE;
    }
    private XLSXHandler() {
    }

    public static void writeStudentsListToExcel(List<SimplifiedProcess> simplProcesses, String path) {

        // Using XSSF for xlsx format
        Workbook workbook = new XSSFWorkbook();
        Sheet tasksSheet = workbook.createSheet("Tasks");

        int rowIndex = 0;

        for (SimplifiedProcess sProc : simplProcesses) {

            Row row = tasksSheet.createRow(rowIndex++);

            int cellIndex = 0;

            //first place in row
            row.createCell(cellIndex++).setCellValue(sProc.getName());

            //second place in row
            row.createCell(cellIndex++).setCellValue(sProc.getMemory() + " KB");
        }

        // Set column width
        tasksSheet.autoSizeColumn(0);
        tasksSheet.autoSizeColumn(1);

       // Create scatter chart


















        // Write workbook in excel file
        try {

            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();

            System.out.println(path + " saved!");

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound" + e);
        } catch (IOException e) {
            System.out.println("InputOutputException" + e);
        }
    }
}
