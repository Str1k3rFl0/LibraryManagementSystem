// 5

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

interface userData
{
    void displayUserDetails(String[] userData);
    void confirmDeleteAccount(String loggedInUser);
}

interface books
{
    void displayAddBookForm(String loggedInUser);
    void displayDeleteBookForm(String loggedInUser);
    void saveBookDetails(String authorName, String bookName, String bookGenre, String bookDate, String bookPages, String bookCode, String publishedBy);
    boolean deleteBookByCode(String bookCode, String loggedInUser);

    void displayBooksTable();
    void sortBooks(String selectedCriterion);
}

interface controlButtons
{
    void confExitButton();
}

public class MainLibWindow extends Frame implements userData, books, controlButtons
{
    // Variable for the size of the new window
    final int width = 800;
    final int height = 600;

    // Variable for exit button
    private Button exitButton = new Button("Exit");

    private Authentication auth = new Authentication();
    private String loggedInUser;

    // TEST !!!
    private DefaultTableModel tableModel;
    private JTable booksTable;

    // Constructor
    public MainLibWindow(String loggedInUser)
    {
        // Configuration for the library window
        this.loggedInUser = loggedInUser;
        this.setTitle("Hipodrom Library");
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        // Place the exit button
        confExitButton();

        // Set the menubar
        setMenuBar(new MenuBarLibrary().createMenu(loggedInUser, this));

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent wEvent)
            {
                System.exit(0);
            }
        });

        this.setVisible(true);
    }

    public String getUserRole(String loggedInUser)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] userData = line.split("\\|");
                if (userData[0].equals(loggedInUser))
                {
                    return userData[6];
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return "Client";
    }

    public void showPermissionError(String message)
    {
        // Show permission error message in a dialog
        JOptionPane.showMessageDialog(this, message, "Permission Denied", JOptionPane.ERROR_MESSAGE);
    }

    // Function to configure the exit button
    @Override
    public void confExitButton()
    {
        exitButton.setBounds(350, 500, 100, 30);
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                System.exit(0);
            }
        });
        this.add(exitButton);
    }

    // Function to return the auth
    public Authentication getAuth() { return auth; }

    // Function to display the user details
    @Override
    public void displayUserDetails(String[] userData)
    {
        if (userData != null)
        {
            // Create the window
            Frame userDetailsFrame = new Frame("Account Details");
            userDetailsFrame.setSize(400, 300);
            userDetailsFrame.setLayout(new GridLayout(6, 2, 10, 10));

            // Centre the window
            int x = this.getX() + (this.getWidth() - userDetailsFrame.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - userDetailsFrame.getHeight()) / 2;
            userDetailsFrame.setLocation(x, y);

            // Show the details
            userDetailsFrame.add(new Label("First Name:"));
            userDetailsFrame.add(new Label(userData[0]));

            userDetailsFrame.add(new Label("Last Name:"));
            userDetailsFrame.add(new Label(userData[1]));

            userDetailsFrame.add(new Label("Age:"));
            userDetailsFrame.add(new Label(userData[2]));

            userDetailsFrame.add(new Label("City:"));
            userDetailsFrame.add(new Label(userData[3]));

            userDetailsFrame.add(new Label("Email:"));
            userDetailsFrame.add(new Label(userData[4]));

            // Button to exit the new window for user details
            int buttonWidth = 80;
            int buttonHeight = 25;
            Button closeButton = new Button("Close");
            closeButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            closeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent aEvent)
                {
                    userDetailsFrame.dispose();
                }
            });

            userDetailsFrame.add(closeButton);
            userDetailsFrame.setVisible(true);
        }
        else
        {
            System.out.println("User not found!");
        }
    }

    // Function to confirm to delete the user account
    @Override
    public void confirmDeleteAccount(String loggedInUser)
    {
        int response = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete the account?",
                "Account deletion confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION)
        {
            boolean success = auth.deleteAccount(loggedInUser);
            if (success)
            {
                JOptionPane.showMessageDialog(
                        this, "Account deleted successfully!",
                        "Stergere Cont", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            else
            {
                JOptionPane.showMessageDialog(
                        this, "Error deleting account.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Function to create a new window
    // for adding a new book
    @Override
    public void displayAddBookForm(String loggedInUser)
    {
        // Published by..
        String[] userData = auth.getUserData(loggedInUser);
        if (userData == null)
        {
            JOptionPane.showMessageDialog(this, "Error: User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // First and Last name of the user
        String publishedBy = userData[0] + " " + userData[1];

        Frame addBookFrame = new Frame("Add Book");
        addBookFrame.setSize(450, 400);
        addBookFrame.setLayout(new GridLayout(8, 2, 10, 10));

        // Create a text field for each detail
        TextField authorNameField = new TextField();
        TextField bookNameField = new TextField();
        TextField bookGenreField = new TextField();
        TextField bookDateField = new TextField();
        TextField bookPagesField = new TextField();
        TextField bookCodeField = new TextField();
        TextField pubNameField = new TextField(publishedBy);

        addBookFrame.add(new Label("Author:"));
        addBookFrame.add(authorNameField);

        addBookFrame.add(new Label("Book Name:"));
        addBookFrame.add(bookNameField);

        addBookFrame.add(new Label("Genre Book:"));
        addBookFrame.add(bookGenreField);

        addBookFrame.add(new Label("Date of Publication"));
        addBookFrame.add(bookDateField);

        addBookFrame.add(new Label("Number of Pages:"));
        addBookFrame.add(bookPagesField);

        addBookFrame.add(new Label("Book Code:"));
        addBookFrame.add(bookCodeField);

        pubNameField.setEditable(false);
        addBookFrame.add(new Label("Published by:"));
        addBookFrame.add(pubNameField);

        Button saveButton = new Button("Save");
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                String authorName = authorNameField.getText();
                String bookName = bookNameField.getText();
                String bookGenre = bookGenreField.getText();
                String bookDate = bookDateField.getText();
                String bookPages = bookPagesField.getText();
                String bookCode = bookCodeField.getText();

                // Need to complete all the text fields
                if (!authorName.isEmpty() && !bookName.isEmpty() && !bookGenre.isEmpty() &&
                    !bookDate.isEmpty() && !bookPages.isEmpty() && !bookCode.isEmpty())
                {
                    saveBookDetails(authorName, bookName, bookGenre, bookDate, bookPages, bookCode, publishedBy);
                    addBookFrame.dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(addBookFrame, "All fields are mandatory!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addBookFrame.add(new Label());
        addBookFrame.add(saveButton);

        addBookFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent aEvent)
            {
                addBookFrame.dispose();
                setVisible(true);
            }
        });

        addBookFrame.setLocationRelativeTo(this);
        addBookFrame.setVisible(true);
    }

    // Function to allow the user
    // to delete the book by code
    @Override
    public void displayDeleteBookForm(String loggedInUser)
    {
        Frame deleteBookFrame = new Frame("Delete Book");
        deleteBookFrame.setSize(400, 300);
        deleteBookFrame.setLayout(new GridLayout(4, 2, 10, 10));

        TextField userNameField = new TextField();
        TextField passwordField = new TextField();
        TextField bookCodeField = new TextField();

        passwordField.setEchoChar('*');

        deleteBookFrame.add(new Label("User First Name:"));
        deleteBookFrame.add(userNameField);

        deleteBookFrame.add(new Label("Password:"));
        deleteBookFrame.add(passwordField);

        deleteBookFrame.add(new Label("Book Code:"));
        deleteBookFrame.add(bookCodeField);

        Button deleteButton = new Button("Delete");
        deleteButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                String userName = userNameField.getText();
                String password = passwordField.getText();
                String bookCode = bookCodeField.getText();

                if (auth.validateLogIn(userName, password))
                {
                    if (deleteBookByCode(bookCode, loggedInUser))
                    {
                        JOptionPane.showMessageDialog(deleteBookFrame, "The book has been deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        deleteBookFrame.dispose();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(deleteBookFrame, "The book code was not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(deleteBookFrame, "Incorrect username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBookFrame.add(new Label());
        deleteBookFrame.add(deleteButton);

        deleteBookFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent aEvent)
            {
                deleteBookFrame.dispose();
                setVisible(true);
            }
        });

        deleteBookFrame.setLocationRelativeTo(this);
        deleteBookFrame.setVisible(true);
    }

    // Function to save al details for the book
    @Override
    public void saveBookDetails(String authorName, String bookName, String bookGenre, String bookDate, String bookPages, String bookCode, String publishedBy)
    {
        String filePath = "Books.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true)))
        {
            writer.write(authorName + "|" + bookName + "|" + bookGenre + "|" +
                        bookDate + "|" + bookPages + "|" + bookCode + "|" + publishedBy);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "The book has been successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            displayBooksTable();
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Error saving the book.", "Error", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
        }
    }

    // Function to delete the book
    @Override
    public boolean deleteBookByCode(String bookCode, String loggedInUser)
    {
        String filePath = "Books.txt";
        File tempFile = new File("TempBooks.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] bookDetails = line.split("\\|");
                if (bookDetails.length >= 5 && bookDetails[4].equals(bookCode))
                {
                    found = true;
                }
                else
                {
                    writer.write(line);
                    writer.newLine();
                }
            }

            writer.flush();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }

        if (found)
        {
            File originalFile = new File(filePath);
            if (originalFile.exists())
            {
                originalFile.delete();
            }

            tempFile.renameTo(originalFile);

            // Update the table
            displayBooksTable();
        }

        return found;
    }

    // Function to show all the books
    // in a table
    @Override
    public void displayBooksTable()
    {
        if (tableModel == null)
        {
            // Define table columns
            String[] columnNames = { "Author", "Book Name", "Genre", "Date of Publication", "Nr. of pages", "Code", "Published by" };
            tableModel = new DefaultTableModel(columnNames, 0);
            booksTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(booksTable);

            // Add the table in the main Window
            Panel booksPanel = new Panel();
            booksPanel.setLayout(new BorderLayout());
            booksPanel.add(scrollPane, BorderLayout.CENTER);
            booksPanel.setBounds(50, 50, 700, 400);
            this.add(booksPanel);

            // Add JComboBox for sorting
            String[] sortCriteria = { "Author", "Book Name", "Genre", "Date of Publication", "Nr. of pages", "Code", "Published by" };
            JComboBox<String> sortComboBox = new JComboBox<>(sortCriteria);
            sortComboBox.setBounds(50, 470, 200, 30);
            sortComboBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    String selectedCriterion = (String) sortComboBox.getSelectedItem();
                    // Call the sort function
                    sortBooks(selectedCriterion);
                }
            });
            this.add(sortComboBox);
        }

        // Clear the table before update
        tableModel.setRowCount(0);

        // Read from file to show the books
        String filePath = "Books.txt";
        List<String[]> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] bookDetails = line.split("\\|");
                if (bookDetails.length >= 7)
                {
                    books.add(bookDetails);
                }
            }
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Error reading the book file", "Error", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
            return;
        }

        // Sort books
        books.sort(getBookComparator("Author"));

        // Add sorted books to the table
        for (String[] book : books)
        {
            tableModel.addRow(book);
        }

        this.validate();
        this.repaint();
    }

    private Comparator<String[]> getBookComparator(String selectedCriterion)
    {
        switch (selectedCriterion)
        {
            case "Author":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[0].compareTo(book2[0]); // Sort by author
                    }
                };
            case "Book Name":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[1].compareTo(book2[1]); // Sort by book name
                    }
                };
            case "Book Genre":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[2].compareTo(book2[2]); // Sort by genre
                    }
                };
            case "Year of publication":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[3].compareTo(book2[3]); // Sort by publication year
                    }
                };
            case "Number of pages":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        int pages1 = Integer.parseInt(book1[4]);
                        int pages2 = Integer.parseInt(book2[4]);
                        return Integer.compare(pages1, pages2); // Sort by number of pages
                    }
                };
            case "Code":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[5].compareTo(book2[5]); // Sort by code
                    }
                };
            case "Published by":
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        return book1[6].compareTo(book2[6]); // Sort by published by
                    }
                };
            default:
                return new Comparator<String[]>()
                {
                    public int compare(String[] book1, String[] book2)
                    {
                        // Default to sorting by author
                        return book1[0].compareTo(book2[0]);
                    }
                };
        }
    }

    // Function to sort the books
    @Override
    public void sortBooks(String selectedCriterion)
    {
        // This will re-sort the books based on the selected criterion
        List<String[]> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Books.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] bookDetails = line.split("\\|");
                if (bookDetails.length >= 7)
                {
                    books.add(bookDetails);
                }
            }
        }
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(this, "Error reading the book file", "Error", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
            return;
        }

        // Sort books based on selected criterion
        books.sort(getBookComparator(selectedCriterion));
        // Clear the table before adding sorted data
        tableModel.setRowCount(0);

        // Add sorted books to the table
        for (String[] book : books)
        {
            tableModel.addRow(book);
        }
    }
}