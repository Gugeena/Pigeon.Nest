package com.ldal.pigeonapp;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SQLServer
{
    String url = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7805733";
    static String userName = "sql7805733";
    static String password = "5Ku4wg8YE3";

    PassHasher passHasher = new PassHasher();

    private Connection connection;
    private final PreparedStatement signUpStatement;
    private final PreparedStatement checkLogin;
    private final PreparedStatement checkEmail;
    private final PreparedStatement checkPassword;
    private final PreparedStatement getUserFromLogin;
    private final PreparedStatement getUserFromEmail;
    private final PreparedStatement sendEmailNoAttachment;
    private final PreparedStatement getEmailFromID;
    private final PreparedStatement getUserFromID;
    private final PreparedStatement updatePassword;
    private final PreparedStatement checkBackupPassword;
    private final PreparedStatement updateRecoveryPassword;
    private final PreparedStatement removeAccount;
    private final PreparedStatement getDateCreatedFromLogin;
    private final PreparedStatement DeleteInboxFromID;
    private final PreparedStatement setGmailFromLogin;
    private final PreparedStatement checkGmailFromLogin;
    private final PreparedStatement getGmailFromLogin;
    private final PreparedStatement checkPigeonEmail;
    private final Statement statement;

    public SQLServer()
    {
        try
        {
            String userName = "sql7805733";
            String password = "5Ku4wg8YE3";
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
            dbSetup();
            //statement.execute("use pigeonDB");

            signUpStatement = connection.prepareStatement("Insert into user(login, email, hashedpass, recoverypass, dateCreated) " +
                    "values(?, ?, ?, ?, ?)");

            checkLogin = connection.prepareStatement("Select id from user where login = ?");
            checkEmail = connection.prepareStatement("Select gmailRecovery from user where login = ?");
            checkPigeonEmail = connection.prepareStatement("Select * from user where email = ?");
            checkBackupPassword = connection.prepareStatement("Select recoveryPass from user where login = ?");
            checkPassword = connection.prepareStatement("Select hashedPass from user where login = ?");
            getUserFromLogin = connection.prepareStatement("Select * from user where login = ?");
            getUserFromEmail = connection.prepareStatement("Select * from user where email = ?");
            getUserFromID = connection.prepareStatement("Select * from user where id = ?");
            updatePassword = connection.prepareStatement("update user set hashedPass = ? where login = ?;");
            removeAccount = connection.prepareStatement("delete from user where login = ?");
            getDateCreatedFromLogin = connection.prepareStatement("Select dateCreated from user where login = ?");
            sendEmailNoAttachment = connection.prepareStatement("Insert into mails(senderID, receiverID, emailText, sendTime, subject) " +
                    "values(?, ?, ?, ?, ?)");
            getEmailFromID = connection.prepareStatement("select * from mails where receiverID = ? order by id desc");
            checkGmailFromLogin = connection.prepareStatement("select gmailRecovery from user where login = ?");
            DeleteInboxFromID = connection.prepareStatement("delete from mails where receiverID = ? and senderID = ?");
            updateRecoveryPassword = connection.prepareStatement("update user set recoveryPass = ? where login = ?");
            setGmailFromLogin = connection.prepareStatement("update user set gmailRecovery = ? where login = ?");
            getGmailFromLogin = connection.prepareStatement("select gmailRecovery from user where login = ?");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkerGmail(String login)
    {
        try
        {
            checkGmailFromLogin.setString(1, login);
            ResultSet resultSet = checkGmailFromLogin.executeQuery();
            if(resultSet.next())
            {
                String gmail = resultSet.getString("gmailRecovery");
                if(gmail != null && !gmail.isEmpty()) return true;
                else return false;
            }
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getterGmail(String login)
    {
        try
        {
            getGmailFromLogin.setString(1, login);
            ResultSet dbResult = getGmailFromLogin.executeQuery();
            dbResult.next();
            return dbResult.getString("gmailRecovery");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void dbRemover() throws SQLException
    {
        statement.execute("drop database pigeonDB");
    }

    private void dbSetup() throws SQLException
    {
        //statement.execute("create database if not exists pigeonDB");
        //statement.execute("use pigeonDB");
        statement.execute("create table if not exists user(" +
                "id int primary key auto_increment," +
                "login varchar(20)," +
                "email varchar(50)," +
                "hashedPass mediumtext," +
                "recoveryPass varchar(12)," +
                "dateCreated varchar(30)," +
                "gmailRecovery varchar(255)," +
                "isAdmin bool," +
                "isBanned bool" +
                ")");
        statement.execute("create table if not exists mails(" +
                "id int primary key auto_increment," +
                "senderID int," +
                "receiverID int," +
                "emailText mediumtext," +
                "attachment mediumblob," +
                "sendTime datetime," +
                "subject varchar(75)" +
                ")");
    }

    public void sqlStatement(String query){

        try {
            statement.execute(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeRecoverypass(String recoverypass, String login)
    {
        try
        {
            updateRecoveryPassword.setString(1, passHasher.hasher(recoverypass));
            updateRecoveryPassword.setString(2, login);
            updateRecoveryPassword.executeUpdate();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public boolean validateLogin(String login){
        try{
            checkLogin.setString(1, login);
            ResultSet dbResult = checkLogin.executeQuery();

            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateEmail(String email)
    {
        try{
            checkEmail.setString(1, email);
            ResultSet dbResult = checkEmail.executeQuery();

            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validatePigeonEmail(String email)
    {
        try{
            checkPigeonEmail.setString(1, email);
            ResultSet dbResult = checkPigeonEmail.executeQuery();

            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

   public boolean validateRecoveryPass(String login, String recoveryPass)
   {
       try
       {
           checkBackupPassword.setString(1, login);
           ResultSet dbResult = checkBackupPassword.executeQuery();
           if (dbResult.next())
           {
               return passHasher.hasher(recoveryPass).equals(dbResult.getString("recoveryPass"));
           }
           return false;
       } catch (SQLException e) {throw new RuntimeException(e);}
   }

    public void changePassword(String c_hashedPass, String login)
    {
        try
        {
            updatePassword.setString(1, c_hashedPass);
            updatePassword.setString(2, login);
            updatePassword.executeUpdate();
        }
        catch (SQLException e) {throw new RuntimeException(e);}
    }

    public boolean validatePassword(String login, String c_hashedPass){
        try{
            checkPassword.setString(1, login);
            ResultSet dbResult = checkPassword.executeQuery();
            dbResult.next();
            return c_hashedPass.matches(dbResult.getString("hashedPass"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(int sender, int receiver, String text, String subject) {
        try {
            sendEmailNoAttachment.setInt(1, sender);
            sendEmailNoAttachment.setInt(2, receiver);
            sendEmailNoAttachment.setString(3, text);

            LocalDateTime localDateTime = LocalDateTime.now();
            String dateTime = Email.dateTimeToString(localDateTime);

            sendEmailNoAttachment.setString(4, String.valueOf(dateTime));
            sendEmailNoAttachment.setString(5, subject);
            sendEmailNoAttachment.execute();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public User userFromEmail(String email){
        try {
            getUserFromEmail.setString(1, email);
            ResultSet user = getUserFromEmail.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean  checkDuplicateLogin(String login){
        try{
            getUserFromLogin.setString(1, login);
            ResultSet dbResult = getUserFromLogin.executeQuery();
            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(String login)
    {
        try
        {
            removeAccount.setString(1, login);
            removeAccount.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean  checkDuplicateEmail(String email){
        try{
            getUserFromEmail.setString(1, email);
            ResultSet dbResult = getUserFromEmail.executeQuery();
            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User userFromID(int id){
        try {
            getUserFromID.setInt(1, id);
            ResultSet user = getUserFromID.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            return new User(-1, "", "DeletedUser");
        }
    }

    public User userFromLogin(String login){
        try {
            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getDateCreatedFromID(String login)
    {
        try
        {
            getDateCreatedFromLogin.setString(1, login);
            ResultSet user = getDateCreatedFromLogin.executeQuery();;
            if (user.next())
            {
                return user.getString("dateCreated");
            }
            else
            {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public User SignUp(String login, String email, String hashedPass, String recoveryPass, String dateCreated){
        try {
            signUpStatement.setString(1, login);
            signUpStatement.setString(2, email);
            signUpStatement.setString(3, hashedPass);
            signUpStatement.setString(4, passHasher.hasher(recoveryPass));
            signUpStatement.setString(5, dateCreated);

            signUpStatement.execute();


            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            int id = user.getInt("id");

            return new User(id, login, email);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public User logIn(String login){
        try {
            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            int id = user.getInt("id");
            String email = user.getString("email");
            if(!user.getBoolean("isAdmin")) return new User(id, login, email);
            else return new Admin(id, login, email);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateUser(String email)
    {
        try
        {
            getUserFromEmail.setString(1, email);
            ResultSet dbResult = getUserFromEmail.executeQuery();
            return dbResult.isBeforeFirst();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateID(int ID)
    {
        try
        {
            getUserFromID.setInt(1, ID);
            ResultSet dbResult = getUserFromID .executeQuery();
            return dbResult.isBeforeFirst();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Email> getInbox(int id){
        try {
            getEmailFromID.setInt(1, id);

            ResultSet dbResult = getEmailFromID.executeQuery();

            ArrayList<Email> inbox = new ArrayList<>();

            while(dbResult.next()){
                User sender = userFromID(dbResult.getInt("senderID"));
                User receiver = userFromID(dbResult.getInt("receiverID"));
                String text = dbResult.getString("emailText");
                String subject = dbResult.getString("subject");
                //Blob attachment = dbResult.getBlob("Attachemnt");
                Timestamp dateTime = dbResult.getTimestamp("sendTime");

                Email email = new Email(sender, receiver, text, subject);
                email.setDateTime(Email.dateTimeToString(dateTime.toLocalDateTime()));

                inbox.add(email);
            }

            return inbox;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteInbox(int id, int id1)
    {
        try
        {
            DeleteInboxFromID.setInt(1, id);
            DeleteInboxFromID.setInt(2, id1);
            DeleteInboxFromID.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void setGmail(String gmail, String login)
    {
        try
        {
            setGmailFromLogin.setString(1, gmail);
            setGmailFromLogin.setString(2, login);
            setGmailFromLogin.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
