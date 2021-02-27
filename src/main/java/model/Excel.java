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
    private static final int DATA_START = 2; // randu de incepere a informatiilor utile
    // COLUMN CONSTANTS (am incercat cu Enum da nu mi place duma cu .ordinal)
    private static final int TAG = 0;
    private static final int DESC = 1;
    private static final int ESDA = 2;
    private static final int ESTI = 3;
    private static final int RETI = 4;
    private static final int VAL = 5;
    private static final int LBL = 6;

    /// EXCEL CONSTATS
    private static final String DB_LOCATION = "tasks2.xlsx";
    public static final String SHEET_NAME = "StandApp";
    public static final String DATE_FORMAT = "dd/MM/yyyy";


    //// this method loads all the information from the Excel file into the app
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

        System.out.println("the LOADED data is :");
        System.out.println(goals);

        return goals;
    }

    //// this method returns the number of rows in our Excel
    private static int getNumberOfRows(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }

    //// this method tests if a row is a GOAL or a TASK
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

        ///----
        //progress
        String progressValueString = row.getCell(VAL).getStringCellValue();
        indexOfSeparator = progressValueString.indexOf("%");
        float progressValue = Float.parseFloat(progressValueString.substring(0, indexOfSeparator));
        progressValue /= 100.0f;
        Progress progress = new Progress(progressValue);
        //----

        // make the link between ::: task <-> goal
        Task resultTask = new Task(goal, description, estimatedTime);
        resultTask.setProgress(progress); ///// !!
        goal.addTask(resultTask); // aici goal e doar o referinta, deci ar trebui sa mearga.

        //System.out.println("task readed from excel : " + resultTask);
        return resultTask;
    }

    //// this method returns the workbook for the Excel file
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

    //// after we create a GOAL on UI, we use this function to insert it in Excel
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

    //// after we create a TASK on UI, we use this function to insert it in Excel
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

    //// we use this function to delete a TASK from Excel.
    // WE DON T DELETE GOALS, BECAUSE ONCE WE HAVE A GOAL, WE AIN'T STOP UNTIL WE SUCCED
    public static void deleteTaskRow(Task selectedTask) {
        ///todo : check duplicates [pe viitor]
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        int taskIndex = findTaskRowIndex(sheet, selectedTask);
        sheet.shiftRows(taskIndex + 1, sheet.getLastRowNum(), -1);

        writeAndCloseWorkbook(workbook);
    }

    //// this method returns the rowIndex from Excel of a specific TASK
    private static int findTaskRowIndex(Sheet sheet, Task selectedTask) {
        int noOfRows = getNumberOfRows(sheet);
        for (int i = DATA_START; i < noOfRows; i++) {
            Row row = sheet.getRow(i);
            Cell tag = row.getCell(TAG); // read row Tag
            if (! isGoal(tag)){ // e task
                String searchedTaskDescription = selectedTask.getDescription().getDescription();
                String excelTaskDescription = row.getCell(DESC).getStringCellValue();
                if (searchedTaskDescription.equals(excelTaskDescription)){ // am gasit goalulu
                    return i;// + 1; // NU STIU DC +1 DAR ASA MERGE
                }
            }
        }

        System.out.println("GOALU NU A FOST GASIT IN EXCEL");
        return  -1;
    }

    //// this method returns the rowIndex from Excel of a specific GOAL
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

    //// this method is used after we pressed the save button for a TASK.
    //// thats when we update the REALIZED TIME and PROGRESS
    public static void updateTaskRow(Task task, String timeWork, String progressWork) {
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        int taskIndex = findTaskRowIndex(sheet, task);

        String stringRealizedTime = sheet.getRow(taskIndex).getCell(RETI).getStringCellValue();
        Time realizedTime = new Time(stringRealizedTime);
        Time currentRealizedTime = new Time(timeWork);
        realizedTime.addTime(currentRealizedTime);
        sheet.getRow(taskIndex).getCell(RETI).setCellValue(realizedTime.toString());
        task.setRealizedTime(realizedTime);

        int index = progressWork.indexOf("%");
        int progress = Integer.valueOf(progressWork.substring(0, index));
        System.out.println("prg : " + progress);
        float pointProgress = progress / 100.0f;
        task.getProgress().updateProgress(pointProgress);

        sheet.getRow(taskIndex).getCell(VAL).setCellValue(progress + "%");
        sheet.getRow(taskIndex).getCell(LBL).setCellValue(task.getProgress().getLabel());

        writeAndCloseWorkbook(workbook);

    }

    //// after many types of operations that involve writing results in Excel
    //// we use this function to write and close the workbook and save that results
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


    //// when this method is called, the GOALs are updated
    public static void updateGoalData(ArrayList<Goal> goals) {
        Workbook workbook = createWorkbook();
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        for (Goal goal : goals){
            int rowIndex = findGoalRowIndex(sheet, goal);
            Excel.updateGoal(sheet, rowIndex, goal);
        }

        writeAndCloseWorkbook(workbook);
    }

    public static void updateGoal (Sheet sheet, int rowIndex, Goal goal){
        sheet.getRow(rowIndex).getCell(ESTI).setCellValue(goal.getEstimatedTime().toString());
        sheet.getRow(rowIndex).getCell(RETI).setCellValue(goal.getRealizedTime().toString());
        sheet.getRow(rowIndex).getCell(VAL).setCellValue(goal.getProcent());
        sheet.getRow(rowIndex).getCell(LBL).setCellValue(goal.getProgress().getLabel());
    }
}
