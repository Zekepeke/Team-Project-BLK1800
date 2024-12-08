package src.gui.pages.auth.signup;

import interfaces.gui.CustomColors;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Represents the sign-up page of the application.
 * It includes user registration functionality and graphical components.
 */
public class SignUpPage extends JPanel implements CustomColors {

    boolean isVisible;
    boolean signUpWasVisible;

    Color backgroundColor = BACKGROUND;

    SignUpUI signUpUI;

    int width;
    int height;

    String usernameString = "Username";
    String passwordString = "Password";

    boolean validUsernameAndPassword = true;

    /**
     * Constructs a new SignUpPage with specified dimensions and a client.
     *
     * @param width  the width of the sign-up page.
     * @param height the height of the sign-up page.
     * @param client the client object for handling server communication.
     */
    public SignUpPage(int width, int height, ClientSide client) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;

        isVisible = true;
        signUpWasVisible = false;


        signUpUI = new SignUpUI(300, 430);
        signUpUI.setPreferredSize(new Dimension(300, 450));

        // Add the login button functionality
        signUpUI.getSignUpButton().addActionListener(e -> {
            usernameString = signUpUI.getUsernameField().getText();
            passwordString = new String(signUpUI.getPasswordField().getPassword());
            System.out.println("From Sign up:");
            System.out.println("Username: " + usernameString);
            System.out.println("Password: " + passwordString);

            System.out.println("Valid: " + ClientSide.validUserAndPassword(usernameString, passwordString));
            validUsernameAndPassword = ClientSide.validUserAndPassword(usernameString, passwordString);

            if (!validUsernameAndPassword) {
                signUpUI.setErrorVisible(true);
            } else {
                int condition = ClientSide.signup(usernameString, passwordString);
                System.out.println("Testing the server search for client: " + condition);

                if(condition == ClientSide.USER_EXISTS) {
                    signUpUI.getError().setText("That name already exists");
                    signUpUI.setErrorVisible(true);
                }
                else if (condition == ClientSide.SUCCESS)  {
                    ClientSide.username = usernameString;
                    ClientSide.password = passwordString;

                    signUpUI.setErrorVisible(false);
                    signUpUI.setSuccessVisible(true);
                } else if (condition == ClientSide.ERROR) {
                    signUpUI.getError().setText("Something went wrong");
                    signUpUI.setErrorVisible(true);
                }
            }

        });

        add(signUpUI);
        setVisible(false);

        // Add mouse listeners (later)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Handle mouse press
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Handle mouse drag
            }
        });
    }


    /**
     * Paints the graphical components of the SignUpPage.
     *
     * @param g the Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        width = getWidth();
        height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2d.setPaint(backgroundColor);
        g2d.fillRect(0, 0, width, height);
    }

    /**
     * Sets the width of the sign-up page.
     *
     * @param width the new width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the height of the sign-up page.
     *
     * @param height the new height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the username entered by the user.
     *
     * @return the username as a String.
     */
    public String getUsername() {
        return usernameString;
    }

    /**
     * Gets the password entered by the user.
     *
     * @return the password as a String.
     */
    public String getPassword() {
        return passwordString;
    }

    /**
     * Gets the SignUpUI instance for the sign-up page.
     *
     * @return the SignUpUI instance.
     */
    public SignUpUI getSignUpUI() {
        return signUpUI;
    }

}
