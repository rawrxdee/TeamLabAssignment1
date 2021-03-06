package lab2ass;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * MenuDriver
 *
 * @author RAWR-XD
 * @version 1.0
 */
public class MenuDriver {
    private static Course cMain = new Course(); // Main Course List
    private static Person pMain = new Person(); // Main Person List
    private DecimalFormat df = new DecimalFormat("0.00");
    private PrintWriter writer = null;
    private boolean stillRunning;
    private boolean subMenu;
    private Scanner input = new Scanner(System.in);
    private String fileName;
    private Scanner inputFileLoadStudent;

    // PROGRAM ENTRY POINT:
    public static void main(String[] args) {
        MenuDriver theProgram = new MenuDriver();
        theProgram.start();
    }

    /**
     * To check if a date matches the format criteria
     *
     * @param date this is the date to be checked
     * @return A new true or false boolean
     */
    private static boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void generateStudentInvoice(int ID) {
        double priceInvoiceGen = 0.00;
        PrintWriter writer = null;
        Person student = pMain.personList.get(ID);
        try {
            String filename = "StudentInvoice" + student.getName() + ".txt";
            writer = new PrintWriter(filename.replaceAll("\\s", ""), "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {

            e.printStackTrace();
        }


        if (!pMain.personList.get(ID).CourseList.isEmpty()) {
            if (writer != null) {
                writer.println("### Course Invoice For " + student.getName() + " ###");
                for (Course course : student.CourseList) {
                    priceInvoiceGen += course.getPrice();
                    writer.println("Course ID: " + course.getID() + " Course Name: " + course.getName() + " Course Price: $" + course.getPrice() + " Course Runtime: " + course.getRuntime() + " Course Type: " + course.getClass());
                }
                writer.println("Final Total: $" + priceInvoiceGen);
                writer.close();
            }
        }

    }

    private void start() {
        int choice;
        stillRunning = true; // in order to commence program
        while (stillRunning) {
            showMainMenu();
            choice = getUserSelection(0, 8);
            processChoiceMainMenu(choice);
        }

    }

    /**
     * To present a menu/list of options to the user.
     */
    private void showMainMenu() {
        System.out.println();        // ensure a break between previous output and the menu
        System.out.println("What would you like to do?");
        System.out.println("1.  Add a new rate payer");
        System.out.println("2.  Pet's Menu");
        System.out.println("3.  Courses Menu");
        System.out.println("4.  Student Menu");
        System.out.println("5.  Run system tests");
        System.out.println("6.  Save or Load Person/Pet data");
        System.out.println("8.  Facebook Private Infomation");
        System.out.println("0.  Exit Program");
    }

    /**
     * To dispatch control to a relevant method which handles the user's selected choice.
     *
     * @param choice - the code of the menu option selected by the user.
     */
    private void processChoiceMainMenu(int choice) {
        switch (choice) {
            case 1:
                // menu option 1: register person
                Person p1 = personWizard();
                pMain.addPerson(p1);
                System.out.println("New person added: " + p1.getName());
                menuReturn();
                break;
            case 2:
                // menu option 2: pet menu
                subMenu = true;
                while (subMenu) {
                    showPetMenu();
                    int selection = getUserSelection(0, 5);
                    processChoicePetMenu(selection);
                }
                menuReturn();
                break;
            case 3:
                // menu option 3: course menu
                subMenu = true;
                while (subMenu) {
                    showCourseMenu();
                    int selection = getUserSelection(0, 10);
                    processChoiceCourseMenu(selection);
                }
                break;
            case 4:
                // menu option 4: student menu
                subMenu = true;
                while (subMenu) {
                    showStudentMenu();
                    int selection = getUserSelection(0, 9);
                    processChoiceStudentMenu(selection);
                }
                break;
            case 5:
                //menu option 5: system tests
                System.out.println("Running Person Test's");
                TestDriverClass.runTestPerson(null);
                System.out.println("Running Pet Test's");
                TestDriverClass.runTestPet(null);
                System.out.println("Running Course Test's");
                TestDriverClass.runTestCourse(null);
                menuReturn();
                break;
            case 6:
                //menu option 6: save/load data
                System.out.println("What would you like to (s)ave or (l)oad data?");
                String fileChoice = input.nextLine();
                if (fileChoice.equalsIgnoreCase("s")) {

                    try {
                        fileName = "PersonAndPetData.txt";
                        writer = new PrintWriter(fileName, "UTF-8");
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!pMain.personList.isEmpty()) {
                        for (Person person : pMain.personList) {
                            writer.println(person);
                            if (person.hasPet()) {
                                for (Animal pet : person.personPetList) {
                                    writer.println(pet.toString());
                                }
                            }
                        }
                        writer.close();
                        System.out.println("The People and Pet data been saved under the name: " + fileName.replaceAll("\\s", ""));
                    } else {
                        System.out.println("There are no current people saved in the filesystem");
                    }
                } else if (fileChoice.equalsIgnoreCase("l")) {
                    fileName = "PersonAndPetData.txt";
                    String line = "";
                    String cvsSplitBy = ",";

                    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                        while ((line = br.readLine()) != null) {
                            String[] data = line.split(cvsSplitBy);
                            if (data[0].equalsIgnoreCase("person")) {
                                Person person = new Person(data[2], data[1], data[3], data[4]);
                                pMain.addPerson(person);
                            }
                            Person person = pMain.personList.get(pMain.personList.size() - 1);
                            if (data[0].equalsIgnoreCase("cat")) {
                                person.addAPet(new Cat(data[1], data[2], data[3], data[4], data[5], Integer.valueOf(data[6])));
                            } else if (data[0].equalsIgnoreCase("dog")) {
                                person.addAPet(new Dog(data[1], data[2], data[3], data[4], data[5], Integer.valueOf(data[6]), Boolean.getBoolean(data[7]), Boolean.getBoolean(data[8])));
                            } else if (data[0].equalsIgnoreCase("rabbit")) {
                                person.addAPet(new Rabbit(data[1], data[2], data[3], data[4], data[5], Integer.valueOf(data[6])));
                            }
                        }
                        System.out.println("People and Pet data has been loaded from: " + fileName.replaceAll("\\s", ""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                menuReturn();
                break;
            case 7:
                //menu option 8: lizard eggos
                System.out.println("If any errors occour please send an email to Zucc@lizardsquad.com.");
                System.out.println("Gaining access to Lizard Deep Web.");
                System.out.println("\n Would you like to load Facebooks private info on Adrain Shatte \n Type 'Yes' to View & 'No' to Exit "); // asks the user if they would like to send all of the private data to facebook
                String Input = input.nextLine(); // obtain the input
                menuReturn();
                break;
            case 0:
                //quit
                System.out.println("What would you like to QUIT (y)es or (n)o?");
                String quitChoice = input.nextLine();
                if (quitChoice.equalsIgnoreCase("y")) {
                    System.out.println("Goodbye!");
                    stillRunning = false;// causes the main loop of program to end (i.e. quits)
                } else {
                    menuReturn();
                }
                break;
            default:
                //error
                System.out.println("Unexpected selection made. Doing nothing.");
                break;
        }
    }

    /**
     * To obtain from the user a selection (an integer) from a range of values
     *
     * @param lower - the Lowest permissible value the user can enter as their selection.
     * @param upper - the Highest permissible value the user can enter
     * @return userInput The value entered by the user, unless the "lower" parameter was higher than the "upper" parameter, in which case 0 is returned.
     */
    private int getUserSelection(int lower, int upper) {
        int userInput;

        if (lower > upper)
            return 0;

        do {
            System.out.print("Enter a selection (" + lower + "-" + upper + "):");
            userInput = input.nextInt(); // obtain the input
            input.nextLine(); // gets rid of the newline after the number we just read
            if (userInput < lower || userInput > upper)
                System.out.println("Invalid choice.");
        } while (userInput < lower || userInput > upper);
        System.out.println(); // put a space before the next output

        return userInput;
    }

    /**
     * To generate a person.
     *
     * @return A new person object
     */
    private Person personWizard() {
        System.out.println("Enter rate payer name: ");
        String name = input.nextLine(); // obtain the name
        System.out.println("Enter rate payer address: ");
        String address = input.nextLine(); // obtain the address
        System.out.println("Enter rate payer postcode: ");
        String postcode = input.nextLine(); // obtain the postcode
        System.out.println("Enter rate payer city: ");
        String city = input.nextLine(); // obtain the city
        return new Person(address, name, postcode, city);
    }

    private void showStudentMenu() {
        System.out.println();
        System.out.println("1.  View all Students");
        System.out.println("2.  Generate Invoice");
        System.out.println("3.  Enter In Results");
        System.out.println("4.  View Results");
        System.out.println("5.  View Text Doc");
        System.out.println("6.  Save A Student");
        System.out.println("7.  Delete A Student ");
        System.out.println("8.  Load A Student");
        System.out.println("0.  Return to the Main Menu");

    }

    private void processChoiceStudentMenu(int choice) {
        switch (choice) {
            case 1:
                // view students
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        if (!person.CourseList.isEmpty()) {
                            System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                        }
                    }
                }
                break;
            case 2:
                // generate invoice
                if ((!pMain.personList.isEmpty()) && (!Course.courseList.isEmpty())) {
                    System.out.println();
                    for (Person person : pMain.personList) {
                        if (!person.CourseList.isEmpty()) {
                            System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                        }
                    }
                    System.out.println();
                    System.out.println("Enter Student ID:");
                    int ID = input.nextInt(); // obtain the city
                    generateStudentInvoice(ID);
                } else {
                    System.out.println("No Students or Courses");
                }
                break;
            case 3:
                // enter results
                if ((!pMain.personList.isEmpty()) && (!Course.courseList.isEmpty())) {
                    System.out.println();
                    for (Person person : pMain.personList) {
                        if (!person.CourseList.isEmpty()) {
                            System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                        }
                    }
                    System.out.println();
                    System.out.println("Enter Student ID:");
                    int ID1 = input.nextInt();
                    for (Course course : pMain.personList.get(ID1).CourseList) {
                        System.out.println("ID " + course.toStringShort());

                    }
                    System.out.println("Enter Course ID:");
                    int courseID = input.nextInt();
                    System.out.println("Enter In Result:");
                    pMain.personList.get(ID1).CourseList.get(courseID).result = input.nextFloat();
                } else {
                    System.out.println("No Students or Courses");


                }
                break;
            case 4:
                // view results
                if ((!pMain.personList.isEmpty()) && (!Course.courseList.isEmpty())) {
                    System.out.println();
                    for (Person person : pMain.personList) {
                        if (!person.CourseList.isEmpty()) {
                            System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                        }
                    }
                    System.out.println();
                    System.out.println("Enter Student ID:");
                    int ID2 = input.nextInt();
                    for (Course course : pMain.personList.get(ID2).CourseList) {
                        System.out.println("ID " + course.toStringShort());

                    }
                    for (Course course : Course.courseList) {
                        if (!Course.courseList.isEmpty()) {
                            System.out.println("ID " + course.getID() + ": " + course.getName());
                        }
                    }
                    System.out.println("Enter Course ID:");
                    int courseID1 = input.nextInt();
                    System.out.println(pMain.personList.get(ID2).CourseList.get(courseID1).result);
                    break;

                } else {
                    System.out.println("No Students or Courses");


                }
            case 5:
                // view invoice
                Scanner inputFile = null;
                try {
                    inputFile = new Scanner(new File("StudentInvoice.txt"));
                } catch (FileNotFoundException e) {
                    //
                    e.printStackTrace();
                }
                String firstline = inputFile.nextLine();
                System.out.println("Data: " + firstline);
                String firstline2 = inputFile.nextLine();
                System.out.println("Data: " + firstline2);
                break;

            case 6:
                // save a student
                System.out.println();
                System.out.println("Enter Student ID:");
                try {
                    int ID1StudentSave = input.nextInt();

                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter("StudentSave.txt", "UTF-8");
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        //
                        e.printStackTrace();
                    }
                    writer.println(pMain.personList.get(ID1StudentSave).getName());
                    System.out.println(pMain.personList.get(ID1StudentSave).getName());
                    for (Course course : pMain.personList.get(ID1StudentSave).CourseList) {
                        {
                            writer.println("Break");
                            writer.println(course.getName());
                        }
                    }
                } catch (java.lang.IndexOutOfBoundsException exception) {
                    System.out.println("No Name by that ID");
                    break;
                }
                writer.close();
                break;
            case 7:
                // delete student
                System.out.println();
                for (Person person : pMain.personList) {
                    System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                }
                System.out.println();
                System.out.println("Enter In Student ID");
                int studentDelID = input.nextInt();
                for (Course course : pMain.personList.get(studentDelID).CourseList) {
                    System.out.println("ID " + course.toStringShort());

                }
                System.out.println("Enter In Course ID");
                int studentDelIDCourse = input.nextInt();
                pMain.personList.get(studentDelID).CourseList.remove(studentDelIDCourse);
                break;
            case 8:
                // load student
                inputFileLoadStudent = null;
                try {
                    inputFileLoadStudent = new Scanner(new File("StudentSave.txt"));
                } catch (FileNotFoundException e) {
                    //
                    e.printStackTrace();
                }
                String name = inputFileLoadStudent.nextLine();
                for (Person person : pMain.personList) {
                    {

                        if (name.equals(person.getName())) {
                            System.out.println("Data Found:  " + name);
                        }
                    }
                }
                if (inputFileLoadStudent.nextLine().equals("Break")) {
                    System.out.println("Break Found");
                    nextbreak(name);
                }
                break;
            case 0:
                // return to main menu
                subMenu = false;
                menuReturn();
                break;
        }

    }

    //I fixed the indentation on this and I hate whoever put it here
    private void nextbreak(String name) {
        String nextlineString = inputFileLoadStudent.nextLine();
        System.out.println("READING BREAK");
        System.out.println(nextlineString);
        for (Course course : Course.courseList) {
            if (course.getName().equals(nextlineString)) {
                int courseIDPos = course.getID() - 1;
                for (Person person : pMain.personList) {
                    if (person.getName().equals(name)) {
                        person.enrollInCourse(Course.courseList.get(courseIDPos));
                    }
                }
            }
        }
        if (inputFileLoadStudent.hasNextLine()) {
            System.out.println("New Line Found");
            nextbreak(name);
        }
    }

    private void showCourseMenu() {
        System.out.println();
        System.out.println("1.  Create a new Course offering");
        System.out.println("2.  View Course Details");
        System.out.println("3.  Delete current Course offering");
        System.out.println("4.  Enroll a Student in a Course");
        System.out.println("5.  Generate a list of all Courses");
        System.out.println("6.  Generate Expenses Report");
        System.out.println("7.  Edit Course Data");
        System.out.println("8.  Save Course Schema");
        System.out.println("9.  Load Course Schema");
        System.out.println("10. Generate Expenses Report From Course Schema");
        System.out.println("0.  Return to the Main Menu");
    }

    private void processChoiceCourseMenu(int choice) {
        switch (choice) {
            case 1:
                //create new course
                Course c = courseWizard();
                cMain.diffrentCourses(c);
                System.out.println("New course : " + c.toString());
                menuReturn();
                break;
            case 2:
                //get course details
                //FIXME: duplicated code for picking an object
                if (!Course.courseList.isEmpty()) {
                    int index = 0;
                    for (Course course : Course.courseList) {
                        System.out.println(index + ". " + course.getName());
                        index++;
                    }
                    int selectCourse = getUserSelection(0, index--);
                    System.out.println(Course.courseList.get(selectCourse).toString());
                } else {
                    System.out.println("Please add a Course first!");
                }
                menuReturn();
                break;
            case 3:
                //delete a course
                //FIXME: this should be a method at some point
                if (!Course.courseList.isEmpty()) {
                    int index = 0;
                    for (Course course : cMain.getAllCourses()) {
                        System.out.println(index + ". " + course.getName());
                        index++;
                    }
                    int pos = getUserSelection(0, index--);
                    Course.courseList.remove(pos);
                } else {
                    System.out.println("No courses in record");
                }
                menuReturn();
                break;
            case 4:
                // enroll a student in a course
                enrollmentWizard();
                menuReturn();
                break;
            case 5:
                // generate a list of courses
                for (Course course : cMain.getAllCourses()) {
                    System.out.println(course.toString());
                }
                menuReturn();
                break;
            case 6:
                // generate expenses report
                if (!cMain.getAllCourses().isEmpty()) {
                    String report = cMain.courseReport(cMain.getAllCourses());
                    System.out.println(report);
                    System.out.println();
                    String parseLine = "";
                    while (!parseLine.equalsIgnoreCase("y") && !parseLine.equalsIgnoreCase("n")) {
                        System.out.println("would you like to save this report to disk??");
                        parseLine = input.nextLine();
                    }
                    if (parseLine.equalsIgnoreCase("y")) {
                        System.out.println("saving...");
                        File saveLocation = new File("courseReport.txt");
                        try {
                            PrintWriter output = new PrintWriter(saveLocation);
                            output.println(report);
                            output.close();
                        } catch (Exception e) {
                            System.out.println("file error");
                        }
                    }
                } else {
                    System.out.println("No courses in record");
                }
                menuReturn();
                break;
            case 7:
                // edit course
                //FIXME: duplicated code for picking an object
                if (!cMain.getAllCourses().isEmpty()) {
                    int index = 0;
                    for (Course course : cMain.getAllCourses()) {
                        System.out.println(index + ". " + course.getName());
                        index++;
                    }
                    int selectCourse = getUserSelection(0, index--);
                    System.out.println();
                    System.out.println("1.  Course Name");
                    System.out.println("2.  Course Price");
                    System.out.println("3.  Cost");
                    System.out.println("4.  Runtime");
                    System.out.println("5.  Number of Enrollments");
                    System.out.println("0.  Cancel");
                    System.out.println();
                    int selectField = getUserSelection(0, 4);
                    Course course = cMain.getAllCourses().get(selectCourse);
                    switch (selectField) {
                        case 0:
                            break;
                        case 1:
                            System.out.println("enter new name:");
                            course.setName(input.nextLine());
                            break;
                        case 2:
                            course.setPrice(promptValidDouble("enter new price:"));
                            break;
                        case 3:
                            course.setCost(promptValidDouble("enter new cost:"));
                            break;
                        case 4:
                            course.setRuntime(promptValidInt("enter new runtime"));
                            break;
                        case 5:
                            course.setNumStudents(promptValidInt("enter new enrollments"));
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.println("Please add a Course first!");
                }
                menuReturn();
                break;
            case 8:
                // save course
                cMain.saveCourses();
                menuReturn();
                break;
            case 9:
                // load course
                cMain.setCourses(cMain.loadCourses("courses.txt"));
                menuReturn();
                break;
            case 10:
                // expenses from file
                File file = new File("32283ue9823ueoiurfh");//_needs_ to be a bad file name for validation to work
                while (!file.exists()) {
                    System.out.println("enter name of file to load");
                    try {
                        file = new File(input.nextLine());
                    } catch (Exception e) {
                    }
                }
                ArrayList courses = cMain.loadCourses(file.getName());
                String report = cMain.courseReport(courses);
                System.out.println(report);
                String parseLine = "";
                while (!parseLine.equalsIgnoreCase("y") && !parseLine.equalsIgnoreCase("n")) {
                    System.out.println("would you like to save this report to disk??");
                    parseLine = input.nextLine();
                }
                if (parseLine.equalsIgnoreCase("y")) {
                    System.out.println("enter filename to save");
                    File saveLocation = new File(input.nextLine());
                    try {
                        PrintWriter output = new PrintWriter(saveLocation);
                        output.println(report);
                        output.close();
                    } catch (Exception e) {
                        System.out.println("file error");
                    }
                }
                menuReturn();
                break;
            case 0:
                // return to menu
                subMenu = false;
                menuReturn();
                break;
            default:
                //error
                System.out.println("Unexpected selection made. Doing nothing.");
                break;
        }
    }

    /**
     * To present the sub pet menu/list of options to the user.
     *
     * @author RAWR-XD (Nathan Blaney)
     */
    private void showPetMenu() {
        System.out.println();
        System.out.println("1.  Register new Pet");
        System.out.println("2.  List Pet(s)");
        System.out.println("3.  Modify Pet(s)");
        System.out.println("4.  Remove Pet(s)");
        System.out.println("5.  Generate Registration Costs");
        System.out.println("0.  Exit");
    }

    /**
     * To process the sub pet menu/list of options to the user.
     *
     * @author RAWR-XD (Nathan Blaney)
     */
    private void processChoicePetMenu(int choice) {
        switch (choice) {
            case 1:
                // menu option 1: register pet
                //print a list of people
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                    }
                    System.out.println("Please enter a Person's ID");
                    //figure out who to give a pet to
                    Person petOwner = pMain.personList.get(getUserSelection(0, pMain.personList.size() - 1));
                    //add the pet
                    if (petOwner.personPetList.size() < 5) {
                        petOwner.addAPet(petWizard());
                        Animal petAdded = petOwner.personPetList.get(petOwner.personPetList.size() - 1);
                        System.out.println("The added pet is: " + petAdded.getName());
                        System.out.println("For the Person: " + petOwner.getName());
                    } else {
                        System.out.println(petOwner.getName() + " already has 5 pet's listed");
                        menuReturn();
                        break;
                    }
                } else {
                    System.out.println("Please add a Person first!");
                    menuReturn();
                    break;
                }
                menuReturn();
                break;
            case 2:
                // menu option 2: list pet(s)
                //print a list of pet(s) for a person
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                    }
                    System.out.println("Please enter a Person's ID");
                    Person petOwner = pMain.personList.get(getUserSelection(0, pMain.personList.size() - 1));
                    if (petOwner.hasPet()) {
                        System.out.println(petOwner.getName() + " has the following pet(s)");
                        for (Animal pet : petOwner.personPetList) {
                            System.out.println("A " + pet.getType() + ", " + pet.getName() + " the " + pet.getBreed());
                        }
                    } else {
                        System.out.println(petOwner.getName() + " does not own a pet!");
                    }

                } else {
                    System.out.println("Please add a Person first!");
                    menuReturn();
                    break;
                }
                menuReturn();
                break;
            case 3:
                // menu option 3: modify pet(s)
                //print a list of pet(s) for a person then give the option to modify
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                    }
                    System.out.println("Please enter a Person's ID");
                    Person petOwner = pMain.personList.get(getUserSelection(0, pMain.personList.size() - 1));
                    if (petOwner.hasPet()) {
                        int index = 0;
                        System.out.println(petOwner.getName() + " has the following pet(s)");
                        for (Animal pet : petOwner.personPetList) {
                            System.out.println("A " + pet.getType() + ", " + pet.getName() + " the " + pet.getBreed() + " (" + index++ + ")");
                        }
                        System.out.println("Please enter the Pets ID");
                        int pos = input.nextInt();
                        input.nextLine();
                        Animal pet = petOwner.personPetList.get(pos);
                        if (pet.getType().equalsIgnoreCase("dog")) {
                            Dog dog = (Dog) pet;
                            System.out.println(dog.getName() + " is currently listed as desexed being " + dog.isDesexed());
                            System.out.println("Has the animal been desexed? (true/false)");
                            dog.setDesexed(input.nextBoolean());
                            System.out.println(dog.getName() + " is currently listed as vaccinated being " + dog.isVaccinated());
                            System.out.println("Has the animal been vaccinated? (true/false)");
                            dog.setVaccinated(input.nextBoolean());
                        } else {
                            System.out.println("Currently only dog entries can be altered");
                        }
                    } else {
                        System.out.println(petOwner.getName() + " does not own a pet!");
                    }

                } else {
                    System.out.println("Please add a Person first!");
                    menuReturn();
                    break;
                }
                menuReturn();
                break;
            case 4:
                // menu option 4: remove pet(s)
                //print a list of pet(s) for a person then give the option to remove
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                    }
                    System.out.println("Please enter a Person's ID");
                    Person petOwner = pMain.personList.get(getUserSelection(0, pMain.personList.size() - 1));
                    if (petOwner.hasPet()) {
                        int index = 0;
                        System.out.println(petOwner.getName() + " has the following pet(s)");
                        for (Animal pet : petOwner.personPetList) {
                            System.out.println("A " + pet.getType() + ", " + pet.getName() + " the " + pet.getBreed() + " (" + index++ + ")");
                        }
                        System.out.println("Please enter the Pets ID");
                        int pos = input.nextInt();
                        input.nextLine();
                        System.out.println("Are you sure you want to remove the pet (y)es or (n)o?");
                        String quitChoice = input.nextLine();
                        if (quitChoice.equalsIgnoreCase("y")) {
                            petOwner.personPetList.remove(pos);
                        } else {
                            menuReturn();
                        }
                    } else {
                        System.out.println(petOwner.getName() + " does not own a pet!");
                    }

                } else {
                    System.out.println("Please add a Person first!");
                    menuReturn();
                    break;
                }
                menuReturn();
                break;
            case 5:
                // menu option 5: calculate rego
                if (!pMain.personList.isEmpty()) {
                    for (Person person : pMain.personList) {
                        System.out.println("ID " + person.getPersonID() + ": " + person.getName());
                    }
                    System.out.println("Please enter a Person's ID");
                    Person ratePayer = pMain.personList.get(getUserSelection(0, pMain.personList.size() - 1));
                    if (ratePayer.hasPet()) {
                        try {
                            fileName = "RegoInvoice" + ratePayer.getName() + ".txt";
                            writer = new PrintWriter(fileName.replaceAll("\\s", ""), "UTF-8");
                        } catch (FileNotFoundException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        double totalRate = 0;
                        System.out.println(ratePayer.getName() + " has " + ratePayer.personPetList.size() + " pet(s)");
                        writer.println(ratePayer.getName() + " has " + ratePayer.personPetList.size() + " pet(s)");
                        for (Animal pet : ratePayer.personPetList) {
                            System.out.println(pet.getName() + " the " + pet.getBreed() + ", a type of " + pet.getType());
                            writer.println(pet.getName() + " the " + pet.getBreed() + ", a type of " + pet.getType());
                            System.out.println(pet.getName() + " was first registered: " + pet.regdue);
                            writer.println(pet.getName() + " was first registered: " + pet.regdue);
                            System.out.println("The rate to pay is: " + df.format(pet.calcRates()));
                            writer.println("The rate to pay is: " + df.format(pet.calcRates()));
                            totalRate = totalRate + pet.calcRates();
                        }
                        System.out.println("Total rates to pay is " + df.format(totalRate));
                        System.out.println("A Text version of this invoice has been saved under the name: " + fileName.replaceAll("\\s", ""));
                        writer.println("Total rates to pay is " + df.format(totalRate));
                    } else {
                        System.out.println(ratePayer.getName() + " does not own a pet!");
                    }
                } else {
                    System.out.println("Please add a Person first!");
                    menuReturn();
                    break;
                }
                writer.close();
                menuReturn();
                break;
            case 0:
                // return to menu
                subMenu = false;
                menuReturn();
                break;
            default:
                //error
                System.out.println("Unexpected selection made. Doing nothing.");
                break;
        }
    }

    /**
     * Wrap Animal creation method in command line interface
     *
     * @return A new pet object
     */
    private Animal petWizard() {
        String creature = "";
        while (!creature.equalsIgnoreCase("c") && !creature.equalsIgnoreCase("d") && !creature.equalsIgnoreCase("r")) {
            System.out.println("Is the pet a (c)at, (d)og, or (r)abbit?");
            creature = input.nextLine();
        }
        System.out.println("What is the breed of the pet?");
        String breed = input.nextLine();
        System.out.println("What is the pet's name?");
        String name = input.nextLine();
        String gender = "";
        while (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
            System.out.println("What is the pet's gender? (m/f)");
            gender = input.nextLine();
        }
        String regdue = "";
        while (!isValidDate(regdue)) {
            System.out.println("When is the animal's first registration? (DD MMM YYYY)");
            regdue = input.nextLine();
        }
        String dob = "";
        while (!isValidDate(dob)) {
            System.out.println("What is the animal's date of birth? (DD MMM YYYY)");
            dob = input.nextLine();
        }
        String chipped = "";
        if (creature.equals("r")) {
            while (!chipped.equalsIgnoreCase("y") && !chipped.equalsIgnoreCase("n")) {
                System.out.println("Is the pet microchipped (y)es or (n)o?");
                chipped = input.nextLine();
            }
        }
        int microchip = 0;
        if (chipped.equalsIgnoreCase("y") || !creature.equalsIgnoreCase("r")) {
            while (microchip <= 0) {
                System.out.println("Microchip number of animal?");
                microchip = input.nextInt();
            }
        }
        switch (creature.toLowerCase()) {
            case "d":
                System.out.println("Has the animal been desexed? (true/false)");
                boolean desexed = input.nextBoolean();
                System.out.println("Has the animal been vaccinated? (true/false)");
                boolean vaccinated = input.nextBoolean();
                return new Dog(breed, name, gender, regdue, dob, microchip, desexed, vaccinated);
            case "c":
                return new Cat(breed, name, gender, dob, regdue, microchip);
            case "r":
                return new Rabbit(breed, name, gender, dob, regdue, microchip);
        }
        return null;
    }

    /**
     * To generate a course.
     *
     * @return A new course object
     */
    private Course courseWizard() {
        String type = "";
        while (!type.equalsIgnoreCase("c") && !type.equalsIgnoreCase("s") && !type.equalsIgnoreCase("e") && !type.equalsIgnoreCase("o")) {
            System.out.println("What type of course do you wish to create?");
            System.out.println("(C)ourse, (S)hortcourse, (E)vening course, or (O)nline Course");
            type = input.nextLine();
        }
        String courseName = "";
        while (courseName.length() <= 0) {
            System.out.println("Enter the Name of new course:");
            courseName = input.nextLine();
        }
        double courseCost = 0.0;
        if (!type.toLowerCase().equals("o")) {
            courseCost = promptValidDouble("enter the cost of " + courseName + " to run");
        }
        double coursePrice = promptValidDouble("Enter the price of " + courseName + ":");
        int courseRuntime = -1;
        while (courseRuntime < 0 && courseRuntime > 10) {
            courseRuntime = promptValidInt("Enter the runtime of " + courseName + ":");
        }
        int courseLecturerID = promptValidInt("Enter the lecturer's id for " + courseName + ":");
        switch (type.toLowerCase()) {
            case "c":
                return new Course(courseLecturerID, courseName, courseCost, coursePrice, courseRuntime);
            case "s":
                return new ShortCourse(courseLecturerID, courseName, courseCost, coursePrice, courseRuntime);
            case "e":
                return new EveningCourse(courseLecturerID, courseName, courseCost, coursePrice, courseRuntime);
            case "o":
                return new OnlineCourse(courseLecturerID, courseName, 0.0, coursePrice, courseRuntime);
        }
        return null;

    }

    /**
     * To enroll a person into a course
     */
    private void enrollmentWizard() {
        int studentID;
        int courseID;
        if (!pMain.personList.isEmpty()) {
            for (Person person : pMain.personList) {
                System.out.println("ID " + person.getPersonID() + ": " + person.getName());
            }
            studentID = promptValidInt("Enter the ID of the student:");
            if (!Course.courseList.isEmpty()) {
                for (Course course : Course.courseList) {
                    System.out.println("ID: " + course.getID() + " Name: " + course.getName());
                }
                System.out.println("Enter the ID of the course you wish to enrol them in: ");
                courseID = input.nextInt();
                Course.courseList.get(courseID).enrollstudent(pMain.personList.get(studentID));
                pMain.personList.get(studentID).enrollInCourse(Course.courseList.get(courseID)); // Gets the selected user and than enrolls them into the selected course
                System.out.println(pMain.personList.get(studentID).getName() + " added to course");
            } else {
                System.out.println("Please add a Course first!");
            }
        } else {
            System.out.println("Please add a Person first!");
        }
    }

    /**
     * Is this string a double
     *
     * @param dub the double to check
     * @return A boolean
     */
    private boolean isDouble(String dub) {
        try {
            Double.parseDouble(dub);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Is this string an int
     *
     * @param wint the int to check
     * @return A boolean
     */
    private boolean isInt(String wint) {
        try {
            Integer.parseInt(wint);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * get an integer
     *
     * @param message what to ask the user
     * @return int
     */
    private int promptValidInt(String message) {
        String parseLine = "dkjfsjieoj";
        while (!isInt(parseLine)) {
            System.out.println(message);
            parseLine = input.nextLine();
        }
        return Integer.parseInt(parseLine);
    }

    /**
     * get an double
     *
     * @param message what to ask the user
     * @return double
     */
    private double promptValidDouble(String message) {
        String parseLine = "wskajfdlk";
        while (!isInt(parseLine)) {
            System.out.println(message);
            parseLine = input.nextLine();
        }
        return Double.parseDouble(parseLine);
    }

    private void menuReturn() {
        try {
            System.out.println("Click enter to return to the menu");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}