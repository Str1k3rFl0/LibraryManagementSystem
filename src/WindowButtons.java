// 2

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class AuthSystem extends Panel
{
    private Frame mainWindow;
    private Frame signUpFrame;

    public AuthSystem(Frame mainWindow)
    {
        this.mainWindow = mainWindow;
    }

    // New window for "SignUp" button
    public void showSignUpForm()
    {
        // Configuration for SignUp window
        signUpFrame = new Frame("SignUp");
        signUpFrame.setSize(400, 420);
        signUpFrame.setLayout(null);

        // Label and text field ( Name1 )
        Label name1Label = new Label("First Name:");
        name1Label.setBounds(50, 50, 120, 30);
        signUpFrame.add(name1Label);

        TextField name1Field = new TextField();
        name1Field.setBounds(180, 50, 160, 30);
        signUpFrame.add(name1Field);

        // Label and text field ( Name2 )
        Label name2Label = new Label("Last Name:");
        name2Label.setBounds(50, 90, 120, 90);
        signUpFrame.add(name2Label);

        TextField name2Field = new TextField();
        name2Field.setBounds(180, 90, 160, 30);
        signUpFrame.add(name2Field);

        // Label and text field ( Age )
        Label ageLabel = new Label("Age:");
        ageLabel.setBounds(50, 130, 120, 30);
        signUpFrame.add(ageLabel);

        TextField ageField = new TextField();
        ageField.setBounds(180, 130, 160, 30);
        signUpFrame.add(ageField);

        // Label and text field ( City )
        Label cityLabel = new Label("City:");
        cityLabel.setBounds(50, 170, 120, 30);
        signUpFrame.add(cityLabel);

        TextField cityField = new TextField();
        cityField.setBounds(180, 170, 160, 30);
        signUpFrame.add(cityField);

        // Label and text field ( Email )
        Label emailLabel = new Label("Email:");
        emailLabel.setBounds(50, 210, 120, 30);
        signUpFrame.add(emailLabel);

        TextField emailField = new TextField();
        emailField.setBounds(180, 210, 160, 30);
        signUpFrame.add(emailField);

        // Label and text field ( Password )
        Label passwordLabel = new Label("Password:");
        passwordLabel.setBounds(50, 250, 120, 30);
        signUpFrame.add(passwordLabel);

        TextField passwordField = new TextField();
        passwordField.setBounds(180, 250, 160, 30);
        passwordField.setEchoChar('*');
        signUpFrame.add(passwordField);

        // Role selection
        Label roleLabel = new Label("Role: ");
        roleLabel.setBounds(50, 290, 120, 30);
        signUpFrame.add(roleLabel);

        Choice roleChoice = new Choice();
        roleChoice.add("Client");
        roleChoice.add("Librarian");
        roleChoice.add("Admin");
        roleChoice.setBounds(180, 290, 160, 30);
        signUpFrame.add(roleChoice);

        // Button for SignUp
        Button submitButton = new Button("Next!");
        submitButton.setBounds(180, 320, 150, 30);
        submitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                String name1 = name1Field.getText();
                String name2 = name2Field.getText();
                int age = Integer.parseInt(ageField.getText());
                String city = cityField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String role = roleChoice.getSelectedItem();

                Authentication auth = new Authentication();
                boolean isSignedUp = auth.signUp(name1, name2, age, city, email, password, role);

                if (isSignedUp)
                {
                    JOptionPane.showMessageDialog(signUpFrame, "Registration successful!");
                }
                else
                {
                    JOptionPane.showMessageDialog(signUpFrame, "Registration failed!");
                }

                signUpFrame.dispose();
                setVisible(true);
            }
        });

        signUpFrame.add(submitButton);
        signUpFrame.setVisible(true);
        signUpFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent wEvent)
            {
                signUpFrame.dispose();
                setVisible(true);
            }
        });

        signUpFrame.setLocationRelativeTo(this);
        setVisible(false);
    }

    // New window for "LogIn" button
    public void showLoginForm()
    {
        // Configurations for window "LogIn User"
        Frame logInFrame = new Frame("LogIn");
        logInFrame.setSize(400, 250);
        logInFrame.setLayout(null);

        // Label and text field ( username )
        Label userLabel = new Label("First Name: ");
        userLabel.setBounds(50, 50, 120, 30);
        logInFrame.add(userLabel);

        TextField userField = new TextField();
        userField.setBounds(180, 50, 160, 30);
        logInFrame.add(userField);

        // Label and text field ( password )
        Label passwordLabel = new Label("Password:");
        passwordLabel.setBounds(50, 100, 120, 30);
        logInFrame.add(passwordLabel);

        TextField passwordField = new TextField();
        passwordField.setBounds(180, 100, 160, 30);
        passwordField.setEchoChar('*');
        logInFrame.add(passwordField);

        Button submitButton = new Button("LogIn!");
        submitButton.setBounds(180, 150, 150, 30);
        submitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                String username = userField.getText();
                String password = passwordField.getText();
                Authentication auth = new Authentication();
                User user = auth.logIn(username, password);

                if (user != null)
                {
                    JOptionPane.showMessageDialog(logInFrame, "LogIn successful!");
                    logInFrame.dispose();
                    mainWindow.dispose();

                    MainLibWindow mainLibWindow = new MainLibWindow(username);
                }
                else
                {
                    JOptionPane.showMessageDialog(logInFrame, "LogIn failed!");
                }

                logInFrame.dispose();
                setVisible(true);
            }
        });

        logInFrame.add(submitButton);
        logInFrame.setVisible(true);
        logInFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent aEvent)
            {
                logInFrame.dispose();
                setVisible(true);
            }
        });

        logInFrame.setLocationRelativeTo(this);
        setVisible(false);
    }
}

// Class for window buttons
public class WindowButtons extends AuthSystem
{
    private Frame mainWindow;

    private Button signUpButton = new Button("SignUp");
    private Button logInButton = new Button("LogIn");
    private Button exitButton = new Button("Exit");

    public WindowButtons(Frame window)
    {
        super(window);
        this.mainWindow = window;

        this.setBounds(0, 0, 800, 600);

        // Place the buttons on the window
        confButtonSignUp();
        confButtonLogIn();
        confButtonExit();

        this.setLayout(null);
    }

    // Function to configurate the SignUp button
    private void confButtonSignUp()
    {
        signUpButton.setBounds(300, 170, 200, 60);
        signUpButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                showSignUpForm();
            }
        });
        this.add(signUpButton);
    }

    // Function to configurate the LogIn button
    private void confButtonLogIn()
    {
        logInButton.setBounds(300, 240, 200, 60);
        logInButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                showLoginForm();
            }
        });
        this.add(logInButton);
    }

    // Function to configurate the Exit button
    private void confButtonExit()
    {
        exitButton.setBounds(300, 310, 200, 60);
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                System.exit(0);
            }
        });
        this.add(exitButton);
    }

    // Function the update the theme for bg and buttons
    public void updateTheme(boolean isDarkTheme) {
        Color backgroundColor = isDarkTheme ? Color.DARK_GRAY : Color.WHITE;
        Color textColor = isDarkTheme ? Color.WHITE : Color.BLACK;

        this.setBackground(backgroundColor);

        // Actualizează stilul butoanelor
        signUpButton.setBackground(backgroundColor);
        signUpButton.setForeground(textColor);

        logInButton.setBackground(backgroundColor);
        logInButton.setForeground(textColor);

        exitButton.setBackground(backgroundColor);
        exitButton.setForeground(textColor);
    }

}