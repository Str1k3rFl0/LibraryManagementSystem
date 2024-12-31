// 1

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

interface ConfirmationExit
{
    final String confirmExitMessage = "Are you sure you want to exit the program?";

    void showExitConfirmationDialog();

    default void logExitAction()
    {
        System.out.println("The user confirmed the exit.");
    }
}

interface GreetingWindow
{
    String[] greetingMessages = {
            "Each book is an adventure!",
            "Thought is more powerful than the speed of light!",
            "Explore a world of knowledge!",
            "Learning has no limits!",
            "Each page takes you further!",
            "We wish you a pleasant journey in the world of reading!",
            "Enjoy every book!"
    };

    void showGreetingMessage();

    default String getRandomGreeting()
    {
        Random random = new Random();
        int numMessage = greetingMessages.length;
        int randomIndex = random.nextInt(numMessage);
        String randomMessage = greetingMessages[randomIndex];
        return randomMessage;
    }
}

class BaseWindow extends Frame implements ConfirmationExit
{
    // Variables for Window size
    final int width = 800;
    final int height = 600;

    // Buttons for Confirmation Exit
    private final Button yesButton = new Button("Yes");
    private final Button noButton = new Button("No");

    private WindowButtons winButtons;
    private MenuBarCustom menuBar;

    public BaseWindow()
    {
        this.setTitle("Hipodrom Library");
        this.setSize(width, height);
        this.setLocationRelativeTo(null);

        // Add the buttons
        winButtons = new WindowButtons(this);
        this.add(winButtons);

        // Add the menuBar
        menuBar = new MenuBarCustom(this, winButtons);
        this.setMenuBar(menuBar);

        // Exit program if press X
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent wEvent)
            {
                showExitConfirmationDialog();
            }
        });

        this.setVisible(true);
    }

    // Function to show the confirmation exit dialog
    @Override
    public void showExitConfirmationDialog()
    {
        Dialog exitDialog = new Dialog(this, "Exit Confirmation", true);
        exitDialog.setSize(350, 100);
        exitDialog.setLayout(new FlowLayout());
        exitDialog.setLocationRelativeTo(this);

        Label message = new Label(confirmExitMessage);
        yesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                logExitAction();
                System.exit(0);
            }
        });

        noButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                exitDialog.dispose();
            }
        });

        exitDialog.add(message);
        exitDialog.add(yesButton);
        exitDialog.add(noButton);
        exitDialog.setVisible(true);
    }
}

class AdvancedWindow extends BaseWindow implements GreetingWindow
{
    @Override
    public void showGreetingMessage() {
        Dialog welcomeDialog = new Dialog(this, "Hipodrom Library", true);
        welcomeDialog.setSize(300, 130);
        welcomeDialog.setLayout(new GridLayout(2, 1));

        Label welcomeMessage = new Label("Welcome to the Hipodrom Library", Label.CENTER);
        Label randomGreeting = new Label(getRandomGreeting(), Label.CENTER);

        welcomeDialog.add(welcomeMessage);
        welcomeDialog.add(randomGreeting);
        welcomeDialog.setLocationRelativeTo(this);

        welcomeDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wEvent) {
                welcomeDialog.setVisible(false);
            }
        });

        welcomeDialog.setVisible(true);
    }
}
