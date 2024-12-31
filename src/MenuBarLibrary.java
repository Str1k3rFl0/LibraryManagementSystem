import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBarLibrary
{
    abstract static class MenuItemAction implements ActionListener
    {
        protected MainLibWindow window;
        protected String loggedInUser;

        public MenuItemAction(MainLibWindow window, String loggedInUser)
        {
            this.window = window;
            this.loggedInUser = loggedInUser;
        }

        @Override
        public abstract void actionPerformed(ActionEvent e);
    }

    static class MenuItemDetailsAction extends MenuItemAction
    {
        public MenuItemDetailsAction(MainLibWindow window, String loggedInUser)
        {
            super(window, loggedInUser);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String[] userData = window.getAuth().getUserData(loggedInUser);
            if (userData != null)
            {
                window.displayUserDetails(userData);
            }
            else
            {
                System.out.println("The user was not found!");
            }
        }
    }

    static class MenuItemDeleteAccountAction extends MenuItemAction
    {
        private String role;

        public MenuItemDeleteAccountAction(MainLibWindow window, String loggedInUser, String role)
        {
            super(window, loggedInUser);
            this.role = role;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if ("Admin".equals(role))
            {
                window.confirmDeleteAccount(loggedInUser);
            }
            else
            {
                window.showPermissionError("You do not have the necessary permission to delete the account.");
            }
        }
    }

    static class MenuItemAddBookAction extends MenuItemAction
    {
        private String role;

        public MenuItemAddBookAction(MainLibWindow window, String loggedInUser, String role)
        {
            super(window, loggedInUser);
            this.role = role;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if ("Admin".equals(role) || "Librarian".equals(role))
            {
                window.displayAddBookForm(loggedInUser);
            }
            else
            {
                window.showPermissionError("You do not have the necessary permission to add a book.");
            }
        }
    }

    static class MenuItemDeleteBookAction extends MenuItemAction
    {
        private String role;

        public MenuItemDeleteBookAction(MainLibWindow window, String loggedInUser, String role)
        {
            super(window, loggedInUser);
            this.role = role;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if ("Admin".equals(role) || "Librarian".equals(role))
            {
                window.displayDeleteBookForm(loggedInUser);
            }
            else
            {
                window.showPermissionError("You do not have the necessary permission to delete the book.");
            }
        }
    }

    public MenuBar createMenu(String loggedInUser, MainLibWindow window)
    {
        MenuBar menuBar = new MenuBar();

        Menu mainMenu = new Menu("User");
        MenuItem detailsMenuItem = new MenuItem("Account Details");
        MenuItem deleteAccountMenuItem = new MenuItem("Delete Account");

        Menu booksMenu = new Menu("Books");
        MenuItem viewBooksMenu = new MenuItem("View Books");
        MenuItem addBookMenuItem = new MenuItem("Add Book");
        MenuItem deleteBookMenuItem = new MenuItem("Delete Book");

        String role = window.getUserRole(loggedInUser);

        detailsMenuItem.addActionListener(new MenuItemDetailsAction(window, loggedInUser));

        deleteAccountMenuItem.addActionListener(new MenuItemDeleteAccountAction(window, loggedInUser, role));

        viewBooksMenu.addActionListener(e -> window.displayBooksTable());

        addBookMenuItem.addActionListener(new MenuItemAddBookAction(window, loggedInUser, role));

        deleteBookMenuItem.addActionListener(new MenuItemDeleteBookAction(window, loggedInUser, role));

        mainMenu.add(detailsMenuItem);
        mainMenu.add(deleteAccountMenuItem);

        booksMenu.add(viewBooksMenu);
        booksMenu.add(addBookMenuItem);
        booksMenu.add(deleteBookMenuItem);

        menuBar.add(mainMenu);
        menuBar.add(booksMenu);

        return menuBar;
    }
}
