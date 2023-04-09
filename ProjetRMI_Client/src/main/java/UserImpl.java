

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class UserImpl implements User {

    private  static  UserImpl usrI;

    private static  Set<String> s=new HashSet<String>();
    public TextArea		text; // used to print out chat messages
    public TextField	        data; // used to enter chat messages
    public TextArea	        users;
    public Frame frame;

    public Forum frm = null; 	// reference to the forum remote object
    public int id; 		// identifier of the client allocated by the server

    private  String username;

    public UserImpl(String us) {

        username=us;

        // creation of the GUI
        frame=new Frame();
        frame.setLayout(new BorderLayout());


        JPanel head = new JPanel();
        head.setBorder(new LineBorder(new Color(0, 0, 0)));
        frame.add(head, BorderLayout.NORTH);
        head.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        head.add(panel_1, BorderLayout.WEST);
        panel_1.setLayout(new GridLayout(1, 0, 0, 0));

        JLabel lblogoUser = new JLabel("");
        panel_1.add(lblogoUser);
        try {
            BufferedImage bufferImage_user = ImageIO.read(new File("src/main/resources/logoUser.png"));
            ImageIcon imageIcon_user = new ImageIcon(bufferImage_user.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            lblogoUser.setIcon(imageIcon_user);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JLabel labelCurrentUser = new JLabel(" M/Mme "+username);
        panel_1.add(labelCurrentUser);

       JPanel panelQuitter = new JPanel();
        head.add(panelQuitter, BorderLayout.EAST);

       JButton quiter = new JButton("Quitter");
        quiter.addActionListener(new leaveListener(this));
        panelQuitter.add(quiter);


        text=new TextArea(10,60);
        text.setEditable(false);
        text.setForeground(Color.black);
        frame.add(text,BorderLayout.CENTER);


        JPanel send=new JPanel(new BorderLayout());
        data=new TextField(60);

        JPanel chat=new JPanel(new BorderLayout());

        send.add(data,BorderLayout.CENTER);

        chat.setSize(200,400);
        frame.add(chat,BorderLayout.CENTER);

        JButton write_button = new JButton("Envoyer");
        write_button.setFont(new Font("Arial", Font.BOLD, 18));
        write_button.setForeground(Color.WHITE);
        write_button.setBackground(Color.BLUE);
        write_button.setBorder(BorderFactory.createRaisedBevelBorder());


        write_button.addActionListener(new writeListener(this));
        send.add(write_button,BorderLayout.EAST);


        users=new TextArea(10,10);
        users.setEditable(false);
        users.setFont(new Font("Arial", Font.PLAIN, 12));
        users.setForeground(Color.BLUE);

        JPanel userSConnect= new JPanel(new BorderLayout());
        userSConnect.setBorder(new EmptyBorder(10, 10, 10, 10));
        userSConnect.add(new JLabel("Les utilisateurs connectés "),BorderLayout.NORTH);
        userSConnect.add(users,BorderLayout.CENTER);
        userSConnect.setSize(50,300);
        frame.add(userSConnect,BorderLayout.WEST);
        chat.add(text,BorderLayout.CENTER);
        chat.add(send,BorderLayout.SOUTH);
        text.setBackground(Color.white);
        users.setBackground(Color.white);
        frame.pack();
        frame.setVisible(false);
    }

    public void ecrire(String msg) {





        try {

            if (msg.contains("Vous"))
            {
                text.setForeground(Color.red);
                this.text.append(msg+"\n");
            }
            else
            if(msg.contains("["))
            {
                users.setForeground(Color.BLUE);
                 users.setText("");

                String[] parts = msg.substring(1, msg.length() - 1).split(",");

                for(String p:parts)
                {
                  this.users.append("☺ User"+p.replaceAll("\\[|\\]|:","")+"\n");
                }



            }else
            {
                if(msg.contains(":"));
                {
                    text.setForeground(Color.BLACK);

                    String idu=msg.split(":")[0];
                    String ms=msg.split(":")[1];
                    if(id!=Integer.parseInt(idu)){
                        this.text.append("User"+idu+" : "+ms+"\n");
                    }

                    else{
                        text.setForeground(Color.DARK_GRAY);
                        this.text.append("Moi : "+ms+"\n");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static void main(String args[]) {
        Frame con=new JFrame();

        con.setLayout(new BorderLayout());
        JTextField usr=new JTextField();
        JPanel two=new JPanel(new BorderLayout());
        two.setBorder(new EmptyBorder(40,10,80,10));
        two.add(new JLabel("Username : "),BorderLayout.WEST);
        two.add(usr,BorderLayout.CENTER);
        con.add(two,BorderLayout.CENTER);

  JLabel lg=new JLabel();
        lg.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            BufferedImage bufferImage_user = ImageIO.read(new File("src/main/resources/form.png"));
            ImageIcon imageIcon_user = new ImageIcon(bufferImage_user.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            lg.setIcon(imageIcon_user);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JPanel logo=new JPanel(new BorderLayout());
        logo.add(lg,BorderLayout.NORTH);
        JLabel fr=new JLabel("Bienvenue au Forum");
        fr.setFont(new Font("Arial", Font.BOLD, 24));
       fr.setForeground(Color.BLUE);
       fr.setBackground(Color.YELLOW);
       fr.setHorizontalAlignment(JLabel.CENTER);
        fr.setVerticalAlignment(JLabel.CENTER);
        logo.add(fr,BorderLayout.CENTER);
        con.add(logo,BorderLayout.NORTH);

        con.setPreferredSize(new Dimension(400,300));

        JButton connctB=new JButton("Se connecter");

        connctB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                con.setVisible(false);
                usrI=new UserImpl(usr.getText());
                connectListener c=new connectListener(usrI);

                c.actionPerformed(e);

                usrI.frame.setVisible(true);

            }
        });

        con.add(connctB,BorderLayout.SOUTH);

        con.pack();
        con.setVisible(true);


    }
}

// action invoked when the "connect" button is clicked
class connectListener implements ActionListener {
    UserImpl utilisateur;
    public connectListener (UserImpl i) {
        utilisateur = i;
    }
    public void actionPerformed (ActionEvent e) {
        try {

            Forum server = (Forum) Naming.lookup("//"+ utilisateur.data.getText()+"/Forum");
            utilisateur.frm=server;
            Proxy c=new ProxyImpl(utilisateur);
            utilisateur.id= utilisateur.frm.entrer(c);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}


class writeListener implements ActionListener {
    UserImpl irc;
    public writeListener (UserImpl i) {
        irc = i;
    }
    public void actionPerformed (ActionEvent e) {
        try{
            irc.frm.dire(irc.id,irc.data.getText());
        }catch( Exception ex){
            ex.printStackTrace();
        }
    }
}


class whoListener implements ActionListener {
    UserImpl irc;
    public whoListener (UserImpl i) {
        irc = i;
    }
    public void actionPerformed (ActionEvent e) {
        try {

            irc.frm.qui();
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}


// action invoked when the "leave" button is clicked
class leaveListener implements ActionListener {
    UserImpl usr;
    public leaveListener (UserImpl i) {
        usr = i;
    }
    public void actionPerformed (ActionEvent e) {
        try{
            usr.frm.quiter(usr.id);

        }catch( RemoteException ex){
            ex.printStackTrace();
        }
    }
}
