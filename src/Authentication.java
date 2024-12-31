// 4

import java.io.*;

class User
{
    protected String name1;
    protected String name2;
    protected int age;
    protected String city;
    protected String email;
    protected String password;
    protected String role;

    public User(String name1, String name2, int age, String city, String email, String password, String role)
    {
        this.name1 = name1;
        this.name2 = name2;
        this.age = age;
        this.city = city;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName1() { return name1; }
    public String getName2() { return name2; }
    public int getAge() { return age; }
    public String getCity() { return city; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

class Client extends User
{
    public Client(String name1, String name2, int age, String city, String email, String password, String role)
    {
        super(name1, name2, age, city, email, password, role);
    }

    public String getRole()
    {
        return "Client";
    }
}

class Bibliotecar extends User
{
    public Bibliotecar(String name1, String name2, int age, String city, String email, String password, String role)
    {
        super(name1, name2, age, city, email, password, role);
    }

    public String getRole() { return "Librarian"; }
}

class Admin extends User
{
    public Admin(String name1, String name2, int age, String city, String email, String password, String role)
    {
        super(name1, name2, age, city, email, password, role);
    }

    public String getRole() { return "Admin"; }
}

interface AuthFunctions
{
    final String filePath = "Users.txt";

    boolean signUp(String name1, String name2, int age, String city, String email, String password, String role);
    User logIn(String email, String password);
    String[] getUserData(String name);
    boolean deleteAccount(String name);
    boolean validateLogIn(String name, String password);
}

public class Authentication implements AuthFunctions
{
    private boolean signUp(User user)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true)))
        {
            String userData = String.format("%s|%s|%d|%s|%s|%s|%s",
                    user.getName1(), user.getName2(), user.getAge(),
                    user.getCity(), user.getEmail(), user.getPassword(),
                    user.getRole());
            writer.write(userData);
            writer.newLine();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean signUp(String name1, String name2, int age, String city, String email, String password, String role) {
        User user;
        switch (role.toLowerCase())
        {
            case "client":
                user = new Client(name1, name2, age, city, email, password, "Client");
                break;
            case "librarian":
                user = new Bibliotecar(name1, name2, age, city, email, password, "Librarian");
                break;
            case "admin":
                user = new Admin(name1, name2, age, city, email, password, "Admin");
                break;
            default:
                System.out.println("Invalid role. User not created.");
                return false;
        }
        return signUp(user);
    }

    public User logIn(String name, String password)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] userData = line.split("\\|");
                if (userData[0].equals(name) && userData[5].equals(password))
                {
                    switch (userData[6].toLowerCase())
                    {
                        case "client":
                            return new Client(userData[0], userData[1], Integer.parseInt(userData[2]), userData[3], userData[4], userData[5], "Client");
                        case "librarian":
                            return new Bibliotecar(userData[0], userData[1], Integer.parseInt(userData[2]), userData[3], userData[4], userData[5], "Librarian");
                        case "admin":
                            return new Admin(userData[0], userData[1], Integer.parseInt(userData[2]), userData[3], userData[4], userData[5], "Admin");
                    }
                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error reading the file: " + filePath);
            ioe.printStackTrace();
        }
        return null;
    }

    public String[] getUserData(String name)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] userData = line.split("\\|");
                if (userData[0].equals(name))
                {
                    return userData;
                }
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error reading the file!");
            ioe.printStackTrace();
        }
        return null;
    }

    public boolean deleteAccount(String name)
    {
        File inputFile = new File(filePath);
        File tempFile = new File("temp.txt");

        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] userData = line.split("\\|");
                if (!userData[0].equals(name))
                {
                    writer.write(line);
                    writer.newLine();
                }
                else
                {
                    deleted = true;
                }
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }

        if (deleted)
        {
            if (inputFile.delete())
            {
                if (tempFile.renameTo(inputFile))
                {
                    return true;
                }
                else
                {
                    System.out.println("Error renaming temporary file.");
                }
            }
            else
            {
                System.out.println("Error deleting the original file");
            }
        }
        else
        {
            System.out.println("The account was not found.");
        }

        return false;
    }

    public boolean validateLogIn(String name, String password)
    {
        return logIn(name, password) != null;
    }
}