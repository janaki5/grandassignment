// //import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileOutputStream;
// //import java.io.FileReader;
// import java.util.ArrayList;
// //import java.util.Arrays;
// import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
public class rstapp{
    static String writeData;
    static File NewFile = new File("orderDetails.csv");
    public static void main(String[] args) {
        while (true) {
            getData menuListObject = new getData("menuList.csv", 3);
            getData billingDetailsObject = new getData("orderDetails.csv", 5);
            String[][] menuData = menuListObject.overallData; //to get the all data in menuList file in 2d array
            String[][] bilingData = billingDetailsObject.overallData; //to get the all data in billingDetails.csv file in 2d array
            MainMenu(); //to show the main menu
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            sc.nextLine();
            //sc.close();
            if(choice==1) {
                int[] enteredData = dataEntry();//to take the data from the user in a 2d array
                menuListObject.showDetails(enteredData, menuData); //method to show the bill for getting conformation
                String s = (billingDetailsObject.lastDetails() + 1) + "," + date() + "," + menuListObject.totalBillAmount + ","+writeData;//storing the data for writing in file

                char ch = sc.nextLine().charAt(0); //taking input to approve or cancel the bill
                if (ch == 'y' || ch == 'Y') {
                    billingDetailsObject.write(s, true); //function to write in the csv file using based on given user
                } else {
                    billingDetailsObject.write(s, false);
                }
            }
            else if(choice==2){

                System.out.println("enter bill id");
                int billNo = sc.nextInt();
                sc.nextLine();
                billingDetailsObject.overWrite(billNo);//function to change the approve disapprove
                writingFile(billingDetailsObject.li,NewFile); //function to overrite

            }
            else if (choice==3) {
                todayCollection(bilingData); //function to see the today collection
            }

                System.out.println("enter M to go back to home screen enter any key to exit");

                char mainMenuNav = sc.nextLine().charAt(0); //input for to nagivite main menu
                if(mainMenuNav=='c'|| mainMenuNav=='C'){
                    continue;
                }
                else{
                    break;
                }
            

        }
        

    }

    public static String date(){  //function to get the date
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String currentDate= myDateObj.format(myFormatObj);
        return currentDate;
    }
    public static void MainMenu(){  //function to shoe the main menu
        System.out.println("Welcome to taj Banjara cafe,Hyderabad");
        System.out.println("-------------------------------------");
        System.out.println("1.Enter a new Order");
        System.out.println("2.Edit Bill Status");
        System.out.println("3.See Collection Of Day\n");
        System.out.println("Please Enter Your choice\n");
    }
    public static int[] dataEntry(){  //data entry function in main class to get input from user;
        Scanner sc  = new Scanner(System.in);
        System.out.println("enter no of quantity");
        int count = sc.nextInt();
        int[] enteredData = new int[count * 2];
        int itemNo = 0;
         writeData = "";
        for (int i = 0; i < count * 2; i += 2) {
            System.out.println("Enter menu id for" + (itemNo + 1) + " item");
            int menuId = sc.nextInt();
            System.out.println("Enter quantity for" + (itemNo + 1) + " item");
            int quantity = sc.nextInt();
            writeData += menuId + " " + quantity + " ";
            enteredData[i] = menuId;
            enteredData[i + 1] = quantity;
            itemNo++;
            //2sc.close();
        }
        return enteredData;
    }
    public static void todayCollection(String[][] bilingData){ //function to calculate today collection in restuarent collection
        double totalCollection =0;
        String date = date();
        for(int i=0;i<bilingData.length;i++){
                if(bilingData[i][1].equals(date) && bilingData[i][4].equals("approved")){
                    totalCollection+=Double.parseDouble(bilingData[i][2]);
            }
        }
        System.out.println("collection on day "+date+" is");
        System.out.println(totalCollection+"Rs");

    }
    public static void writingFile(ArrayList<String> li,File NewFile){ //overwriting the data in file
        try {

            FileOutputStream FileWrite = new FileOutputStream("billingDetails.csv",
                    false);
            FileWrite.close();
            if (NewFile.exists()){
                System.out.println("Data Modified");
            }
            if (NewFile.createNewFile()) {
                System.out.println("Status updated in file");
            }
            FileOutputStream FileWrite2 = new FileOutputStream("billingDetails.csv",
                    true);

            for (String listString : li) {
                listString += "\n";
                byte[] ByteInput = listString.getBytes();
                FileWrite2.write(ByteInput);
            }
            FileWrite2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    

    }
}


class getData{
    String[][] overallData; //creates 2d array for all the data using which object we can create;
    ArrayList<String> li = new ArrayList<>();
    Scanner sc ;
    double totalBillAmount;
    String writeData;
    getData(String fileName,int count){
        this.totalBillAmount=0;
        this.writeData="";
        try {
            File readingFile = new File(fileName);
            sc = new Scanner(readingFile);
            while (sc.hasNext()) {
                li.add(sc.nextLine());  //adding all the strings in the file to the list
            }
            overallData = new String[li.size()][count]; //Initializing the twoD array using count of comas
            for (int i = 0; i < li.size(); i++) {
                String[] words = li.get(i).split(","); //spliting the words using commas and passing each value as a column in a row
                for (int j = 0; j < count; j++) {
                    overallData[i][j] = words[j];
                }
            }
           //3 sc.close();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int lastDetails(){ //to get the last id of object to create a new Order id;
         if (li.size()==0) return 0;

         else {
             String s = overallData[li.size()-1][0];
             return Integer.valueOf(s);
         }
    }
    public void showDetails(int[] enteredData,String[][] data){ //calculating the total bill using the elements entered and menuList.csv data array
        int count =0;
        String format = "|%1$-10s|%2$-40s|%3$-20s|%4$-30s|%5$-30s|\n";
        System.out.format(format,"item","         description        ","quantity","bill for 1 item","total bill");
        for (int i = 0; i < enteredData.length; i+=2) {
            String totalBill = overallData[enteredData[i]-1][2];
            double bill =Double.valueOf(totalBill);
            System.out.format(format,(count+1),"         "+ overallData[enteredData[i]-1][1]+"       ",enteredData[i+1],bill,bill*enteredData[i+1]);
            this.totalBillAmount = this.totalBillAmount+bill*enteredData[i+1];
            count++;
        }

        System.out.println("Total bill amount is    "+this.totalBillAmount);
        System.out.println("Press Y to approve or Any Other Key to cancel");

    }
    public void write(String data,boolean approval){  //function used to write into csv by checking its approved or cancelled
        try {
            FileOutputStream fo = new FileOutputStream("orderDetails.csv",true);
            if(approval==true) data=data+",approved\n";
            else{
                data=data+",canceled\n";
            }
            byte[] arr = data.getBytes();
            fo.write(arr);
            fo.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void overWrite(int billid){ //function to approve and disapprove
        sc = new Scanner(System.in);
        for(int i=0;i<li.size();i++){
            String[] arr = li.get(i).split(",");
            if(Integer.parseInt(arr[0])==billid) {
                System.out.println(li.get(i));
                if (arr[4].equals("approved")) {
                    System.out.println("To cancel press 'C' ");
                    char ch = sc.next().charAt(0);
                    if (ch == 'c' || ch == 'C') {
                        arr[4] = "canceled";
                    }
                } else {
                    if (arr[4].equals("canceled")) {
                        System.out.println("To Approve press 'A' ");
                        char ch = sc.next().charAt(0);
                        if (ch == 'A' || ch == 'a') {
                            arr[4] = "approved";
                        }
                    }


                }
                String toWrite = String.join(",", arr);
                li.set(i, toWrite);
                break;

            }
        }

    }



}
