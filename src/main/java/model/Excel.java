package model;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import java.io.FileInputStream;
import java.util.ArrayList;



public class Excel {
    /// CONSTATS
    private static final String DB_LOCATION = "tasks.xlsx";

    public static ArrayList<Goal> loadGoals (){
        ArrayList<Goal> goals = new ArrayList<>();
        
        Sheet sheet = createWorkbook();
        //System.out.println(sheet.getSheetName());

        return goals;
    }


    private static Sheet createWorkbook() {
        Sheet sheet = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(DB_LOCATION);
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            sheet = workbook.getSheet("StandApp");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"ERROR LOADING THE EXCEL");
            System.out.println("HOPA!");
            System.out.println(e.getMessage());
        }
        return sheet;
    }


}
