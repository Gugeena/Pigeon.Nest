import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Client implements Serializable
{
    private boolean rememberMe;

    @Serial
    private static final long serialVersionUID = 2309L;

    private final String filePath = new File("").getAbsolutePath();

    private final transient SQLServer sqlServer;

    private User user;

    private ArrayList<String> spamblacklist = new ArrayList<>();
    private ArrayList<String> Recepients = new ArrayList<>();

    Client()
    {
        if (!loadSettings())
        {
            //aq default settingebi
            setRememberMe(false);


            saveSettings();
        }

        try
        {
            File sent = new File(filePath + "\\Sent.txt");
            File draft = new File(filePath + "\\Draft.txt");
            File inbox = new File(filePath + "\\Inbox.txt");
            File spam = new File(filePath + "\\Spam.txt");
            File settings = new File(filePath + "\\settings.txt");

            File[] files = {sent, draft, inbox, spam ,settings};


            for (File file : files) {
                if (!file.exists()) {
                    FileWriter fileWriter = new FileWriter(file);
                }
            }

        }
        catch (IOException e) {throw new RuntimeException(e);}
        sqlServer = new SQLServer();
    }

    public boolean loadSettings()
    {
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\settings.txt")));
            Client loadedClient = (Client)objectInputStream.readObject();

            this.rememberMe = loadedClient.rememberMe;
            if(rememberMe)
            {
                this.user = loadedClient.user;
                System.out.println("User loaded, username : " + user.getLogin());
                this.Recepients = loadedClient.Recepients;
            }
            objectInputStream.close();

        }catch (IOException | ClassNotFoundException | NullPointerException e){
            System.out.println("Error while importing settings! Resetting to defaults...");
            return false;
        }
        return true;
    }

    public void saveSettings(){
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\settings.txt")));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            System.out.println("Settings saved!");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public void pages(int pID){
        Scanner scanner = new Scanner(System.in);
        //Select Login or Sign up page
        if(pID == 0){

            if(user != null){
                pages(3);
                return;
            }

            System.out.println("**********Welcome to Pigeon Mail**********\n" +
                    "     1.Log In     \n" +
                    "     2.Sign Up     \n" );


            int ans1 = scanner.nextInt();

            if(ans1 == 1 || ans1 == 2)pages(ans1);
            else {
                System.out.println("Invalid Input!");
                pages(0);
            }
        }

        //Login Page
        else if(pID == 1){
            System.out.println("**********Log In**********\n");

            String login;

            loginLoop:
            while(true) {
                System.out.print("Enter Login : ");
                login = scanner.nextLine();
                if(!User.validateLogin(login)) {
                    System.out.println("Invalid Login! Login can only contain : a-z, A-Z, 0-9, _ and be 4-25 characters long");
                    continue loginLoop;
                }

                if(sqlServer.validateLogin(login)) break;
                else System.out.println("User with this login not found!");
            }

            passwordLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Password : ");
                String password = scanner.nextLine();
                PassHasher passHasher = new PassHasher();
                String c_hashedPass = passHasher.hasher(password);
                if(sqlServer.validatePassword(login, c_hashedPass)){
                    System.out.println("Login successful!");
                    break passwordLoop;
                }
                else System.out.println("Wrong Password!");
            }

            System.out.println();
            System.out.println("Remember me? (Currently : " + this.rememberMe + ")");

            rememberMeLoop:
            while(true){
                String remMe = scanner.nextLine();
                if(remMe.equalsIgnoreCase("true")){
                    setRememberMe(true);
                    saveSettings();
                    break rememberMeLoop;
                }
                else if(remMe.equalsIgnoreCase("false")){
                    setRememberMe(false);
                    saveSettings();
                    break rememberMeLoop;
                }
                else System.out.println("Invalid input! Use \"True\" or \"False\"!");
            }
            System.out.println("**************************");

            user = sqlServer.logIn(login);
            if(rememberMe) saveSettings();

            if(user.isBanned()){
                System.out.println("You are banned!");
                pages(8);
            }
            else pages(3);
        }

        //Sign up page
        else if (pID == 2){
            System.out.println("**********Sign Up**********\n");

            String login;
            loginLoop:
            while(true) {
                System.out.print("Enter Login : ");
                login = scanner.nextLine();
                if(!User.validateLogin(login)) {
                    System.out.println("Invalid Login! Login can only contain : a-z, A-Z, 0-9, _, - and be 4-25 characters long");
                    continue loginLoop;
                }

                if(sqlServer.validateLogin(login)) {
                    System.out.println("Login already taken!");
                    continue loginLoop;
                }
                break loginLoop;
            }

            String password;
            passwordLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Password : ");
                password = scanner.nextLine();
                if(!User.validatePassword(password)){
                    System.out.println("Invalid Password! Password can only contain the alphabet, numbers and special characters(Except for \\ and \") and be 8-25 characters long");
                    continue passwordLoop;
                }
                break passwordLoop;

            }

            PassHasher passHasher = new PassHasher();
            String hashedPass = passHasher.hasher(password);
            String recoveryPass = passHasher.backuppassword();


            String email;
            emailLoop:
            while(true) {
                System.out.println();
                System.out.print("Enter Email (@pigeon.com will be auto-added) : ");
                email = scanner.nextLine();
                if(!User.validateEmail(email)) {
                    System.out.println("Invalid Email! Email can only contain : a-z, A-Z, 0-9, _, - and be 4-20 characters long");
                    continue emailLoop;
                }
                email += "@pigeon.com";
                break emailLoop;
            }

            user = sqlServer.SignUp(login, email, hashedPass, recoveryPass);
            pages(3);
        }

        //Email Hub
        else if (pID == 3){

            System.out.println("********Welcome, " + user.getLogin() +  " ********");
            System.out.println();
            System.out.println("1.Compose E-mail");
            System.out.println("2.Inbox");
            System.out.println("3.Folders");
            System.out.println("4.Settings");
            if(user.isAdmin())System.out.println("\n5.Admin Dashboard");

            int decision = scanner.nextInt();

            if(decision == 1) {
                pages(4);
                return;
            }
            if(decision == 2){
                pages(5);
                return;
            }
            if(decision == 3) {
                FolderAccess();
                return;
            }
            if(decision == 4){
                pages(6);
                return;
            }

            if(decision == 5){
                pages(9);
                return;
            }

            System.out.println("*******************************");
        }

        //Email composer
        else if (pID == 4){
            try
            {
                StringBuilder log = new StringBuilder();

                if(Recepients.isEmpty()) System.out.print("To: ");
                else
                {
                    System.out.print("To (Recommended - ");
                    for(String str : Mostcommonrecepeints(Recepients))
                    {
                        System.out.print(str + ";");
                    }
                    System.out.print("):");
                }

                StringBuilder toField = new StringBuilder();
                String receiver1 = "";
                ArrayList<String> everyreceiver;
                receiverLoop:
                while(true)
                {
                    String strreceiver = scanner.nextLine() + ";";
                    everyreceiver = Indorgroupchecker(strreceiver);
                    for (String s : everyreceiver) {
                        if (!sqlServer.validateUser(s)) {
                            System.out.println("Invalid email: " + s + " ");
                            continue receiverLoop;
                        }
                    }
                    receiver1 = strreceiver;
                    break;
                }
                log.append(receiver1).append("\n");

                for (String recEmail : everyreceiver)
                {
                    toField.append(recEmail).append("\n");
                }

                log.append("To:\n").append(toField.toString());

                System.out.print("Subject: ");
                log.append("Subject: ");
                String subject = scanner.nextLine();
                log.append(subject).append("\n");

                System.out.println("---------------------------");
                log.append("---------------------------\n");

                String text = scanner.nextLine();
                log.append(text).append("\n");
                if (text.length() > 6000) throw new InvalidEmailException("Limit of 6000 characters has been exceeded");

                System.out.println("Destination: ");
                System.out.println("1.Send");
                System.out.println("2.Draft");
                int decision = scanner.nextInt();
                if(decision != 1 && decision != 2) throw new InvalidEmailException("Invalid response");

                else if(decision == 1)
                {
                    for (String recEmail : everyreceiver)
                    {
                        EmailSender(new Email(this.user, sqlServer.userFromEmail(recEmail), text, subject));

                    }
                }

                else
                {
                    for (String recEmail : everyreceiver)
                    {
                        EmailWriter(new Email(this.user, sqlServer.userFromEmail(recEmail), text, subject), "Draft");
                    }
                }
            }
            catch (InvalidEmailException e) {
                System.out.println("ERROR: " + e.getMessage());
                pages(4);
            }
        }

        //Inbox
        else if(pID == 5){
            System.out.println("Inbox action: ");
            System.out.println("1.View inbox");
            System.out.println("2.Delete all in inbox ");

            int decision1 = scanner.nextInt();

            if(decision1 == 1) Folderviewer(4);
            else if(decision1 == 2) FolderDeleter(4);
        }

        //Settings
        else if(pID == 6){
            System.out.println("*****Settings*****");
            System.out.println();
            System.out.println("1.Remember Me");
            System.out.println("2.Log Out");

            int decision1 = scanner.nextInt();

            if(decision1 == 1) pages(7);
            else if(decision1 == 2) pages(8);
        }

        //Settings/RememberMe
        else if(pID == 7){
            System.out.println("*****Remember Me*****");
            System.out.println("Currently set to : " + rememberMe);
            rememberMeLoop:
            while(true){
                String remMe = scanner.nextLine();
                if(remMe.equalsIgnoreCase("true")){
                    setRememberMe(true);
                    saveSettings();
                    break rememberMeLoop;
                }
                else if(remMe.equalsIgnoreCase("false")){
                    setRememberMe(false);
                    saveSettings();
                    break rememberMeLoop;
                }
                else System.out.println("Invalid input! Use \"True\" or \"False\"!");
            }
            System.out.println("**************************");
            pages(6);

        }

        //Settings/LogOut
        else if(pID == 8){
            rememberMe = false;
            user = null;
            saveSettings();
            pages(0);
        }

        else if(pID == 9){
            System.out.println("*****Admin Dashboard*****");
            System.out.println("1.Get user info");
            System.out.println("2.Ban user");
            System.out.println("3.Unban user");

            int dec1 = scanner.nextInt();

            if(dec1 == 1){
                System.out.println("Choose method :" +
                        "\n1.Get by ID" +
                        "\n2.Get by Email" +
                        "\n3.Get by Login");

                int dec2 = scanner.nextInt();

                if(dec2 == 1){
                    System.out.print("Enter user ID : ");
                    int userID = scanner.nextInt();

                    System.out.println(sqlServer.userFromID(userID).toString());
                }
                else if(dec2 == 2){
                    System.out.println("Enter user Email : ");
                    String userEmail = scanner.nextLine();

                    System.out.println(sqlServer.userFromEmail(userEmail));
                }
                else if(dec2 == 3) {
                    System.out.println("Enter user login : ");
                    String userLogin = scanner.nextLine();

                    System.out.println(sqlServer.userFromLogin(userLogin));
                }
                else{
                    pages(9);
                    return;
                }
            }

            if(dec1 == 2){
                System.out.println("Enter user ID : ");
                int userID = scanner.nextInt();


            }
        }
    }

    public void EmailWriter(Email email, String filename)
    {
        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\" + filename + ".txt", true)));
            objectOutputStream.writeObject(email);
            objectOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("E-mail Uploaded to " + filename);
    }

    public void EmailGetter(ArrayList<Email> emails,  String filename)
    {
        try
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "\\" + filename + ".txt")));

            for(Email email : emails)
            {
                objectOutputStream.writeObject(email);
            }
            objectOutputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(filename + "s saved!");
    }

    public void EmailSender(Email email)
    {
            try
            {
                User sender = this.user;
                User receiver = sqlServer.userFromEmail(email.getReceiver().getEmail());

                sqlServer.sendEmail(sender.getId(), receiver.getId(), email.getText(), email.getSubject());

                //Sentebshi shenaxva
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Sent.txt", true));
                bufferedWriter.append(email.toString());
                bufferedWriter.write("\n---EMAIL-END---\n");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        pages(3);
    }

    public void FolderAccess()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select folder: ");
        System.out.println();
        System.out.println("1.Spam");
        System.out.println("2.Draft");
        System.out.println("3.All E-mails sent");

        int decision = scanner.nextInt();
        System.out.println("*******************************");
        if(decision == 1)
        {
            System.out.println("Spam folder action: ");
            System.out.println();
            System.out.println("1.View all in spam");
            System.out.println("2.Delete all in spam");
            System.out.println("3.Add a new user in the spamlist");

            int decision1 = scanner.nextInt();
            System.out.println("*******************************");

            if (decision1 == 1) Folderviewer(1);
            if (decision1 == 2) FolderDeleter(1);
            if (decision1 == 3) Newadditiontospam();
        }
        else if(decision == 2)
        {
            System.out.println("Draft folder action: ");
            System.out.println();
            System.out.println("1.View all in Draft");
            System.out.println("2.Delete all in Draft");

            int decision1 = scanner.nextInt();
            System.out.println("*******************************");

            if (decision1 == 1) Folderviewer(2);
            if (decision == 2) FolderDeleter(2);
        }
        else if(decision == 3)
        {
            System.out.println("All-Emails sent folder action: ");
            System.out.println();
            System.out.println("1.View all in All-Emails sent");
            System.out.println("2.Delete all in All-Emails sent (this will only delete the emails on your own end)");

            int decision1 = scanner.nextInt();

            System.out.println("*******************************");

            if(decision1 == 1) Folderviewer(3);
            if(decision1 == 2) FolderDeleter(3);
        }
    }
    public void Newadditiontospam()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Spam-List user: ");

        String decision = scanner.nextLine();

        spamblacklist.add(decision);
        System.out.println(decision + " added to the spam-List");
        System.out.println("*******************************");
        pages(3);
    }
    public void FolderDeleter(int decision)
    {
        if(decision == 1)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter( filePath + "\\Spam.txt"));
                bufferedWriter.write("");
                System.out.println("Spam folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 2)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Draft.txt"));
                bufferedWriter.write("");
                System.out.println("Draft folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 3)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Sent.txt"));
                bufferedWriter.write("");
                System.out.println("Sent folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        else if(decision == 4)
        {
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "\\Inbox.txt"));
                bufferedWriter.write("");
                System.out.println("Inbox folder cleared out");
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        pages(3);
    }
    public void Folderviewer(int decision)
    {

        Scanner scanner = new Scanner(System.in);
        StringBuilder currentmail = new StringBuilder();

        if(decision == 1)
        {
            ArrayList<Email> spamemails = getFolder("Spam");

            inboxLoop:
            while(true) {
                for (int i = 0; i < spamemails.size(); i++) {
                    Email email = spamemails.get(i);
                    System.out.println(i + 1 + ". " + email.getSubject() + " | From : " + email.getSender().getEmail());
                }

                System.out.print("Choose email (hub : -1) : ");
                int emailChoice = scanner.nextInt();
                if(emailChoice == -1) break inboxLoop;
                else if (emailChoice > 0 && emailChoice <= spamemails.size()) {
                    System.out.println("\n*******************************");
                    System.out.println("\n" + spamemails.get(emailChoice - 1).toString() + "\n");
                    System.out.println("*******************************");
                }
                else System.out.println("Invalid choice!");
            }
        }
        else if (decision == 2)
        {
            ArrayList<Email> draftEmails = getFolder("Draft");

            draftLoop:
            while(true) {
                for (int i = 0; i < draftEmails.size(); i++) {
                    Email email = draftEmails.get(i);
                    System.out.println(i + 1 + ". " + email.getSubject() + " | To : " + email.getReceiver().getEmail());
                }

                System.out.print("Choose email (hub : -1) : ");
                int emailChoice = scanner.nextInt();
                if(emailChoice == -1) break draftLoop;
                else if (emailChoice > 0 && emailChoice <= draftEmails.size()) {
                    System.out.println("\n*******************************");
                    System.out.println("\n" + draftEmails.get(emailChoice - 1).toString() + "\n");
                    System.out.println("*******************************");

                    System.out.println("\n1. Send" +
                            "\n2. Delete from drafts" +
                            "\n3. Back");

                    int input = scanner.nextInt();
                    if(input == 1){
                        EmailSender(draftEmails.get(emailChoice-1));
                    }
                    else if (input == 2) {
                        draftEmails.remove(emailChoice - 1);
                    }

                }
                else System.out.println("Invalid choice!");
            }
        }
        else if(decision == 3)
        {
            ArrayList<Email> sentEmails = getFolder("Sent");

            draftLoop:
            while(true)
            {
                for (int i = 0; i < sentEmails.size(); i++) {
                    Email email = sentEmails.get(i);
                    System.out.println(i + 1 + ". " + email.getSubject() + " | To : " + email.getReceiver().getEmail());
                }

                System.out.print("Choose email (hub : -1) : ");
                int emailChoice = scanner.nextInt();
                if(emailChoice == -1) break draftLoop;
                else if (emailChoice > 0 && emailChoice <= sentEmails.size()) {
                    System.out.println("\n*******************************");
                    System.out.println("\n" + sentEmails.get(emailChoice - 1).toString() + "\n");
                    System.out.println("*******************************");

                    System.out.println("\n1. Send" +
                            "\n2. Delete from sent" +
                            "\n3. Back");

                    int input = scanner.nextInt();
                    if(input == 1){
                        EmailSender(sentEmails.get(emailChoice-1));
                    }
                    else if (input == 2) {
                        sentEmails.remove(emailChoice - 1);
                    }

                }
                else System.out.println("Invalid choice!");
            }
        }
        else if(decision == 4)
        {
            ArrayList<Email> emails = sqlServer.getInbox(this.user.getId());

            inboxLoop:
            while(true) {
                for (int i = 0; i < emails.size(); i++) {
                    Email email = emails.get(i);
                    System.out.println(i + 1 + ". " + email.getSubject() + " | From : " + email.getSender().getEmail());
                }

                System.out.print("Choose email (hub : -1) : ");
                int emailChoice = scanner.nextInt();
                if(emailChoice == -1) break inboxLoop;
                else if (emailChoice > 0 && emailChoice <= emails.size()) {
                    System.out.println("\n*******************************");
                    System.out.println("\n" + emails.get(emailChoice - 1).toString() + "\n");
                    System.out.println("*******************************");
                }
                else System.out.println("Invalid choice!");
            }
        }
        pages(3);
    }

    private ArrayList<Email> getFolder(String foldername)
    {
        ArrayList<Email> Emails = new ArrayList<>();
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath + "\\" + foldername + ".txt")));

            Email cEmail = (Email) objectInputStream.readObject();
            while(cEmail != null)
            {
                Emails.add(cEmail);
                cEmail = (Email) objectInputStream.readObject();
            }

        }
        catch (EOFException eof){
            System.out.println(foldername + "s read!");
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return Emails;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public ArrayList<String> Mostcommonrecepeints(ArrayList<String> recepients)
    {
        HashMap<String, Integer> frequencymap = new HashMap<>();
        for(String  str : recepients)
        {
            frequencymap.put(str, frequencymap.getOrDefault(str, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(frequencymap.entrySet());

        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        ArrayList<String> topapperances = new ArrayList<>();
        for(int i = 0; i < Math.min(3, sortedList.size()); i++)
        {
            topapperances.add(sortedList.get(i).getKey());
        }
        return topapperances;
    }

    public ArrayList<String> Indorgroupchecker (String string)
    {
        StringBuilder currentUser = new StringBuilder();
        ArrayList<String> Users = new ArrayList<>();

        for (Character c : string.toCharArray())
        {
            if (c.equals(';'))
            {
                Users.add(currentUser.toString().trim());
                currentUser.setLength(0);
            }
            else
            {
                currentUser.append(c);
            }
        }
        if (currentUser.length() > 0)
        {
            Users.add(currentUser.toString().trim());
        }
        return Users;
    }

}
