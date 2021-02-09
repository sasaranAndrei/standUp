package model;


import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Excel {
    // ROW CONSTANTS
    private static final int HEADER = 1;
    private static final int DATA_START = 2;
    // COLUMN CONSTANTS (am incercat cu Enum da nu mi place duma cu .ordinal)
    private static final int TAG = 0;
    private static final int DESC = 1;
    private static final int ESDA = 2;
    private static final int ESTI = 3;
    private static final int RETI = 4;
    private static final int VAL = 5;
    private static final int LBL = 6;



    /// CONSTATS
    private static final String DB_LOCATION = "tasks.xlsx";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SHEET_NAME = "StandApp";




    public static ArrayList<Goal> loadGoals (){
        // init excel reading stuff
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        int noOfRows = sheet.getLastRowNum() + 1;
        System.out.println(noOfRows);

        // goals & tasks logic
        ArrayList<Goal> goals = new ArrayList<>(); // result list
        Goal currentGoal = null;
        ArrayList<Task> currentTasks = new ArrayList<>();

        for (int row = DATA_START; row < noOfRows; row++){
            Row currentRow = sheet.getRow(row);
            if (emptyRow(currentRow)) { // EMPTY ROW = e un rand de pauza intre goaluri
                //todo: saveGoalWithHisTask & reinitCurrentGoalAndHisTask
                System.out.println("TAG -> empty row -> save");

            }

            else { // ABSTRACT GOAL ROW = e un rand in care sunt info legate de Goal / Task
                //todo: if (goal) currentGoal = goal, else currentTasks += task
                Cell tag = currentRow.getCell(TAG); // read row Tag
                System.out.println("TAG -> " + tag);

                if (isGoal(tag)){ // daca e goal
                    currentGoal = createGoalByRow(currentRow);

                }
                else { // daca e task
                    //Task currentTask = createTaskByRow(currentRow, currentGoal);
                    //currentTasks.add(currentTask);
                }

            }



        }

        return goals;
    }

    private static boolean isGoal(Cell tag) {
        /// poate o sa o pun in parsing...
        //todo: use regex?
        Pattern pattern = Pattern.compile("goal", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tag.getStringCellValue());
        return matcher.find();
    }

    //// get info from excel and convert into GOAL for Model Data Management
    private static Goal createGoalByRow(Row row) {
        Date estimatedDate = row.getCell(ESDA).getDateCellValue();
        String descriptionString = row.getCell(DESC).getStringCellValue();
        Description description = new Description(descriptionString, estimatedDate);

        Goal resultGoal = new Goal(description);
        return resultGoal;
    }

    //// get info from excel and convert into TASK for Model Data Management
    private static Task createTaskByRow (Row row, Goal goal){
        ///todo some parsing....
        // description
        Date estimatedDate = row.getCell(ESDA).getDateCellValue();
        String descriptionString = row.getCell(DESC).getStringCellValue();
        Description description = new Description(descriptionString, estimatedDate);

        //estimatedTime
        String estimatedTimeString = row.getCell(ESTI).getStringCellValue();
        int indexOfSeparator = estimatedTimeString.indexOf(':');
        int hours = Integer.parseInt(estimatedTimeString.substring(0, indexOfSeparator));
        int minutes = Integer.parseInt(estimatedTimeString.substring(indexOfSeparator, estimatedTimeString.length()));
        Time estimatedTime = new Time(hours, minutes);

        Task resultTask = new Task(goal, description, estimatedTime);
        return resultTask;
    }

    private static boolean emptyRow (Row row){
        if (row == null) System.out.println("wtf romania");
        return row.getCell(TAG) == null;
    }

    private static Workbook createWorkbook() {
        Workbook workbook = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(DB_LOCATION);
            workbook = WorkbookFactory.create(fileInputStream);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"ERROR LOADING THE EXCEL");
            System.out.println("HOPA!");
            System.out.println(e.getMessage());
        }
        return workbook;
    }

    public static void insertGoal(Goal newGoal) {
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        int lastRowIndex = sheet.getLastRowNum();

        int newRowIndex = lastRowIndex + 2; // lasam un rand liber si incepem dupa el
        Row newRow = sheet.createRow(newRowIndex);

        // n a mers cu functie ca aveam
        // nevoie de workook. aparent pepste tot ai nevoie de el orice incerci sa faci

        //////////////////////////////////////////////////////// start of populate
        Cell cell;
        // tag
        cell = newRow.createCell(TAG);
        cell.setCellValue("Goal");
        // description
        cell = newRow.createCell(DESC);
        cell.setCellValue(newGoal.getDescription().getDescription());
        // estimated date
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
        cell = newRow.createCell(ESDA);
        cell.setCellValue(newGoal.getDescription().getEstimatedDate());
        cell.setCellStyle(cellStyle);

        // estimated time
        cell = newRow.createCell(ESTI);
        cell.setCellValue(newGoal.getEstimatedTime().toString());
        // realized time
        cell = newRow.createCell(RETI);
        cell.setCellValue(newGoal.getRealizedTime().toString());

        // value
        //todo daca nu mere bine treaba revin la value simplu si aia e
        cell = newRow.createCell(VAL);
        cell.setCellValue(newGoal.getProcent());
        // label
        cell = newRow.createCell(LBL);
        cell.setCellValue(newGoal.getProgress().getLevel());
        //////////////////////////////////////////////////////// end of populate

        // scriem rezultatele
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(DB_LOCATION);
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
