package name.javalex.model;

import name.javalex.entities.SimplifiedProcess;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class XLSXHandler {

    // Make a single instance to prevent multiple write access to the file
    private static final XLSXHandler INSTANCE = new XLSXHandler();

    public static XLSXHandler getInstance() {
        return INSTANCE;
    }

    public void writeStudentsListToExcel(List<SimplifiedProcess> simplProcesses, String path) {

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(OPCPackage.open(getClass().getResourceAsStream("/tmp.xlsx")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        //Sheet tasksSheet = workbook.createSheet("Tasks");
        Sheet tasksSheet = workbook.getSheetAt(0);

        //Clear temp values
        for (int i = 1; i < 170; i++) {
            try {
                tasksSheet.getRow(i).removeCell(tasksSheet.getRow(i).getCell(1));
                tasksSheet.getRow(i).removeCell(tasksSheet.getRow(i).getCell(2));
            } catch (NullPointerException e) {
                break;
            }
        }

        int rowIndex = 1;

        for (SimplifiedProcess sProc : simplProcesses) {

            Row row = tasksSheet.createRow(rowIndex++);

            int cellIndex = 1;

            //first place in row
            row.createCell(cellIndex++).setCellValue(sProc.getName());

            //second place in row
            row.createCell(cellIndex++).setCellValue(sProc.getMemory());
        }

        // Set column width
        tasksSheet.autoSizeColumn(1);
        tasksSheet.autoSizeColumn(2);

        // Write workbook in excel file
        try {

            FileOutputStream fos = new FileOutputStream(path);
            workbook.write(fos);
            fos.close();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound" + e);
        } catch (IOException e) {
            System.out.println("InputOutputException" + e);
        }
    }
}
