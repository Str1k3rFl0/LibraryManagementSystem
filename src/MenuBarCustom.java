import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class CustomMenuBar extends MenuBar
{
    protected Frame window;

    public CustomMenuBar(Frame window)
    {
        this.window = window;
    }

    protected Menu createMenu(String title)
    {
        return new Menu(title);
    }

    protected MenuItem createMenuItem(String title, ActionListener listener)
    {
        MenuItem item = new MenuItem(title);
        item.addActionListener(listener);
        return item;
    }
}

public class MenuBarCustom extends CustomMenuBar
{
    private Color defaultColor;
    private WindowButtons winButtons;

    public MenuBarCustom(Frame window, WindowButtons winButtons)
    {
        super(window);
        this.defaultColor = Color.WHITE;
        this.winButtons = winButtons;
        createMenuBar();
    }

    public void createMenuBar()
    {
        Menu optionsMenu = createMenu("Options");

        MenuItem resetBgColor = createMenuItem("Reset to default background", new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                window.setBackground(defaultColor);
                window.setForeground(Color.WHITE);
                winButtons.updateTheme(false);
            }
        });

        MenuItem bgTheme = createMenuItem("Change theme (Dark/White)", new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                if (window.getBackground().equals(defaultColor) || window.getBackground().equals(Color.WHITE))
                {
                    window.setBackground(Color.DARK_GRAY);
                    window.setForeground(Color.WHITE);
                    winButtons.updateTheme(true);
                }
                else
                {
                    window.setBackground(defaultColor);
                    window.setForeground(Color.BLACK);
                    winButtons.updateTheme(false);
                }
            }
        });

        optionsMenu.add(resetBgColor);
        optionsMenu.add(bgTheme);

        Menu helpMenu = createMenu("Help");
        MenuItem aboutItem = createMenuItem("About the application", new ActionListener()
        {
            public void actionPerformed(ActionEvent aEvent)
            {
                showAbout();
            }
        });

        helpMenu.add(aboutItem);

        this.add(optionsMenu);
        this.add(helpMenu);
    }

    private void showAbout()
    {
        Dialog aboutDialog = new Dialog(window, "About the application", true);
        aboutDialog.setSize(400, 100);

        Panel about = new Panel(new GridLayout(3, 1));
        about.add(new Label("The application for managing a library named 'Hippodrome Library'"));
        about.add(new Label("was created by the student Nedelcu Flavius Mihai, TI, YEAR II, ULBS, 2024"));

        aboutDialog.setLocationRelativeTo(window);
        aboutDialog.add(about);
        aboutDialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent wEvent)
            {
                aboutDialog.setVisible(false);
            }
        });
    }
}