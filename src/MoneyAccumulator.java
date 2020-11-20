import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class Accumulator
{
    private JPanel GUI;
    //Options:
    private JMenu menu;
    private JMenuItem exit;
    //MenuItems:
    private JButton deposit;
    //Labels
    private JLabel currentTotal, amount;
    private static double nTotal = 0;
    private static File moneyTotalFile;
    //Constructor which takes in String arguments intended for use with main passing in a filename argument.
    public Accumulator(String[] args) {
        try {

            if (args.length > 0) {
                moneyTotalFile = new File(args[0]);
                if(!moneyTotalFile.createNewFile()) {
                    Scanner fileIn = new Scanner(moneyTotalFile);
                    fileIn.useDelimiter("[=\\n]+");

                    //Using TREEMAP to SORT by DATE next TIME and then store the values by TREEMAP also
                    while (fileIn.hasNext()) {
                        //Means there is only one record:
                        if(fileIn.next().equalsIgnoreCase("Total"))
                        {
                            String reader = fileIn.next();
                            nTotal = Double.parseDouble(reader);
                        }
                    }
                    fileIn.close();
                }
            }
            //Otherwise, open a Dialogue Box for Visual aid with selecting files.
            else
                {
                    System.out.println("No file name passed.");
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

                    //ASK USER TO LOAD A FILE FIRST OTHERWISE ASK WHERE TO SAVE FILE
                    if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    {
                        File fOpen = fileChooser.getSelectedFile();
                        moneyTotalFile = new File(fOpen.toString());
                        Scanner fileIn = new Scanner(moneyTotalFile);
                        fileIn.useDelimiter("[=\\n]+");

                        //Using TREEMAP to SORT by DATE next TIME and then store the values by TREEMAP also
                        while (fileIn.hasNext()) {
                            //Means there is only one record:
                            if(fileIn.next().equalsIgnoreCase("Total"))
                            {
                                String reader = fileIn.next();
                                nTotal = Double.parseDouble(reader);
                            }
                        }
                        fileIn.close();
                    }

                    else
                    {
                        System.out.println("No file loaded...");
                        //call to public static method dialog box.
                        saveDialogBox();
                    }
                }
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
        }

    }
    //Dialog Box for saving file.
    public static void saveDialogBox()
    {
        String fileName = "";
        //Dialog Box type File chooser for saving files (or to input where to save files)
        JFileChooser fileChooser = new JFileChooser();
        final JDialog OPEN = new JDialog(); //To focus dialog box
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        System.out.println("Please choose the destination of your save file (a pop-up window will appear): ");

        //holds filename for saving the appointments into text file.
        //Refer to NOTE(1) in the Description section at the top of THIS .java file
        if(fileChooser.showSaveDialog(OPEN) == JFileChooser.APPROVE_OPTION)
        {
            moneyTotalFile = fileChooser.getSelectedFile();
            fileName = moneyTotalFile.getPath();
            System.out.println(fileName);
            save(fileName);
        }
    }
    //For saving File.
    public static void save(String filepath) {
        try {
            File saveFile = new File(filepath);
            if (saveFile.createNewFile())
            {
                PrintWriter writeToFile = new PrintWriter(filepath);
                writeToFile.println("Total=" + getTotal());
                writeToFile.close();
            }
            else {
                FileWriter fileWriter = new FileWriter(filepath, true);
                PrintWriter writeToFile = new PrintWriter(fileWriter);
                writeToFile.println("Total=" +getTotal());
                writeToFile.close();
            }
        }
        catch(IOException io)
        {
            System.out.println("An IO Exception has been caught");
            io.getStackTrace();
            io.printStackTrace();
        }
    }
    //public static void load() -- ADD THIS FEATURE NEXT TIME.

    //Calculate
    public void add(double nTotal)
    {
        Accumulator.nTotal += nTotal;
    }

    //Accessor
    public static double getTotal() {
        return nTotal;
    }

    //returns the file.
    public static File getFile()
    {
        return moneyTotalFile;
    }

}

public class MoneyAccumulator extends JFrame {

    public static void main(String[] args) {
        // write your code here
        Accumulator moneyAcc = new Accumulator(args);
        Scanner userIn = new Scanner(System.in);
        System.out.println("Select options:\na. Get Current Total\nb. Add more money\nq. Quit program");
        String strRead = "";
        strRead = userIn.nextLine();
        while(!strRead.equalsIgnoreCase("q"))
        {
            switch (strRead.toUpperCase())
            {
                case "A"->
                        {
                            System.out.println("The current total is: " + "$" + Accumulator.getTotal());
                        }
                case "B" ->
                        {
                            System.out.println("Enter amount to add to the total " + "(Current Total: " + "$" +Accumulator.getTotal() + "): ");
                            try{
                                double currentAmount = userIn.nextDouble();
                                userIn.nextLine();
                                while(currentAmount < 0) {
                                    System.out.println("Please enter a valid amount");
                                    currentAmount = userIn.nextDouble();
                                    userIn.nextLine();
                                }
                                moneyAcc.add(currentAmount);
                                System.out.println("The new, added amount is: " + "$" + Accumulator.getTotal());
                                /*if(args.length > 0)
                                    Accumulator.save(args[0]);
                                else {
                                    //File newPath = new File("E:\\#2 - MySites-Darwish\\My Java Programs\\Money Counter - Accumulator\\src");
                                    //Accumulator.save(newPath.getPath());
                                }*/
                                if(Accumulator.getFile() == null) {
                                    System.out.println("No File Found!");
                                    Accumulator.saveDialogBox();
                                }
                                else {
                                    Accumulator.save(Accumulator.getFile().getPath());
                                }
                            }
                            catch(IllegalArgumentException | InputMismatchException | NullPointerException | ArithmeticException | ArrayIndexOutOfBoundsException i)
                            {
                                i.getStackTrace();
                                i.printStackTrace();
                            }
                        }
            }
            System.out.println("Select options:\na. Get Current Total\nb. Add more money\nq. Quit program");
            strRead = userIn.nextLine();
        }
        System.out.println("Thank you for using the program! Your final counter total is: " + "$" +Accumulator.getTotal());
    }

}
