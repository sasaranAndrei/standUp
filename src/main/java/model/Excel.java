package model;


import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Excel {
    // ROW CONSTANTS
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

    private static final String FINISH = "FINISH";

    public static ArrayList<Goal> loadGoals () {
        // init excel reading stuff
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        int noOfRows = getNumberOfRows(sheet);
        System.out.println("No of rows : " + noOfRows);

        // goals & tasks logic
        ArrayList<Goal> goals = new ArrayList<>(); // result list
        Goal currentGoal = null;
        ArrayList<Task> currentTasks = new ArrayList<>();

        for (int row = DATA_START; row < noOfRows; row++){
            Row currentRow = sheet.getRow(row);
            if (currentRow == null) break;

            //todo // if (goal) / if (currentGoal != nill) saveGoalWithHisTasks / reinitCurrentGoalAndHisTasks()  { currentGoal = X, currentTaks = [ ] } //
            Cell tag = currentRow.getCell(TAG); // read row Tag
            if (isGoal(tag)){ // daca e GOAL
                if (currentGoal != null){ // daca avem ce salva (nu e la prima iteratie)
                    //todo => link currentGoal with currentTasks.
                    //nu cred ca merge varianta de mai jos ca practic tu i atribui o referinta
                    //todo => ar trebui sa fac cu CLONE cred. DAR voi incerca sa fac leg
                    // task <-> goal in createTaskByRow
                    //currentGoal.setTasks(currentTasks);
                    goals.add(currentGoal); // saveGoalWithHisTasks
                }
                // reinitCurrentGoalAndHisTasks
                currentGoal = createGoalByRow(currentRow);
                currentTasks.clear();
            }
            //todo currentTasks += X;
            else { // daca e TASK
                Task currentTask = createTaskByRow(currentRow, currentGoal);
                currentTasks.add(currentTask); // nu cred ca o sa mi l mai trbeuieasca
            }
        }
        //todo: -> find a more elegant method
        // save the last goal...
        if (currentGoal != null){ // daca avem ce salva (nu e la prima iteratie)
            //todo => link currentGoal with currentTasks.
            currentGoal.setTasks(currentTasks);
            goals.add(currentGoal); // saveGoalWithHisTasks
        }

        /*
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        System.out.println("the LOADED data is :");
        System.out.println(goals);

        return goals;
    }

    private static int getNumberOfRows(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
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
        int indexOfSeparator = estimatedTimeString.indexOf('|');
        int hours = Integer.parseInt(estimatedTimeString.substring(0, indexOfSeparator));
        int minutes = Integer.parseInt(estimatedTimeString.substring(indexOfSeparator + 1));
        Time estimatedTime = new Time(hours, minutes);

        // make the link between ::: task <-> goal
        Task resultTask = new Task(goal, description, estimatedTime);
        goal.addTask(resultTask); // aici goal e doar o referinta, deci ar trebui sa mearga.

        //System.out.println("task readed from excel : " + resultTask);
        return resultTask;
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

    public static void insertGoalRow(Goal newGoal) {
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        int lastRowIndex = sheet.getLastRowNum();
        int newRowIndex = lastRowIndex + 1;
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
        cell.setCellValue(newGoal.getProgress().getLabel());
        //////////////////////////////////////////////////////// end of populate

        writeAndCloseWorkbook(workbook);
    }

    public static void insertTaskRow (Task newTask) {
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        Goal goalOfNewTask = newTask.getGoal();
        int goalIndex = findGoalRowIndex(sheet, goalOfNewTask);
        int shiftIndex = goalIndex + goalOfNewTask.numberOfTasks(); //  the goal that was just introduced
        int newRowIndex = shiftIndex + 1;
        System.out.println("gIdx : " + goalIndex);
        System.out.println("sIdx : " + shiftIndex);
        System.out.println("rowIdx : " + newRowIndex);
        System.out.println("lastRow : " + sheet.getLastRowNum());
        //todo : verifica sa nu fie ultimu rand
        if (shiftIndex != sheet.getLastRowNum()){
            //todo : noi trebuie sa shiftam intre [goalIndex + goals.tasksSize] si [goalIndex + goals.tasksSize + 1]
            sheet.shiftRows(newRowIndex, sheet.getLastRowNum(), 1); // facem loc de taskul nou ce trebuie adaugat
        }

        Row newRow = sheet.createRow(newRowIndex);
        //todo : SCRIEM INFORMATIA (nou TASK) in Excel la index newRowIndex
        //////////////////////////////////////////////////////// start of populate
        Cell cell;
        // tag
        cell = newRow.createCell(TAG);
        cell.setCellValue("Task");
        // description
        cell = newRow.createCell(DESC);
        cell.setCellValue(newTask.getDescription().getDescription());
        // estimated date
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(DATE_FORMAT));
        cell = newRow.createCell(ESDA);
        cell.setCellValue(newTask.getDescription().getEstimatedDate());
        cell.setCellStyle(cellStyle);

        // estimated time
        cell = newRow.createCell(ESTI);
        cell.setCellValue(newTask.getEstimatedTime().toString());
        // realized time
        cell = newRow.createCell(RETI);
        cell.setCellValue(newTask.getRealizedTime().toString());

        // value
        //todo daca nu mere bine treaba revin la value simplu si aia e
        cell = newRow.createCell(VAL);
        cell.setCellValue(newTask.getProcent());
        // label
        cell = newRow.createCell(LBL);
        cell.setCellValue(newTask.getProgress().getLabel());
        //////////////////////////////////////////////////////// end of populate
        System.out.println("u here ma frend ?");
        writeAndCloseWorkbook(workbook);
    }


    private static int findGoalRowIndex(Sheet sheet, Goal goalOfNewTask) {
        int noOfRows = getNumberOfRows(sheet);
        for (int i = DATA_START; i < noOfRows; i++){
            Row row = sheet.getRow(i);
            Cell tag = row.getCell(TAG); // read row Tag
            if (isGoal(tag)){ // mai ramane de veriricat descriereea
                //String searchedGoalShortDescription = goalOfNewTask.getShortDescription();
                String searchedGoalDescription = goalOfNewTask.getDescription().getDescription();
                String excelGoalDescription = row.getCell(DESC).getStringCellValue();
                if (searchedGoalDescription.equals(excelGoalDescription)){ // am gasit goalulu
                    return i;
                }
            }
        }
        System.out.println("GOALU NU A FOST GASIT IN EXCEL");
        return -1;
    }

    private static void writeAndCloseWorkbook(Workbook workbook){
        // write the results
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

    // finish row bullshit
    private static boolean emptyRow (Row row){
        if (row == null) System.out.println("wtf romania");
        return row.getCell(TAG) == null || row.getCell(TAG).getStringCellValue().equals(FINISH);
    }
    private static void insertFinishRow(Sheet sheet) {
        int lastRowIndex = sheet.getLastRowNum();
        Row finishRow = sheet.createRow(lastRowIndex + 1);
        Cell finishCell = finishRow.createCell(TAG);
        finishCell.setCellValue(FINISH);

    }
    private static void deleteFinishRow(Sheet sheet) {
        int lastRowIndex = sheet.getLastRowNum();
        Row deletedRow = sheet.getRow(lastRowIndex);
        sheet.removeRow(deletedRow);
    }


    public static void shiftRow() { // merge
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        sheet.shiftRows(7,8,1);
        writeAndCloseWorkbook(workbook);
    }
}
