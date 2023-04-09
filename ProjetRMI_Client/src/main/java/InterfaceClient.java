

        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.MouseEvent;
        import java.awt.event.MouseListener;
        import java.awt.event.WindowAdapter;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;
        import java.rmi.Naming;
        import java.rmi.RemoteException;

        import javax.swing.JFrame;
        import javax.swing.JLabel;
        import javax.swing.JList;
        import javax.swing.JOptionPane;
        import javax.swing.JPanel;
        import javax.swing.border.EmptyBorder;



        import javax.swing.JScrollPane;
        import javax.swing.ListCellRenderer;
        import javax.imageio.ImageIO;
        import javax.swing.ImageIcon;
        import javax.swing.JButton;

        import javax.swing.border.LineBorder;


public class InterfaceClient extends JFrame  implements User {


    public TextArea		text; // used to print out chat messages
    public TextField	        data; // used to enter chat messages
    public Frame frame;

    public Forum frm = null; 	// reference to the forum remote object
    public int id;

    /////////////////////
    private JPanel contentPane;
    CardLayout card  = new CardLayout();
    JPanel panelChat;

    private JPanel panelinFor, panelQuitter, panel_1;
    private JLabel labelCurrentUser, lblMember;

    private JButton quiter;

    private String username ;

    private String idUser ;

    JList listUser ;



    private JLabel lblogoUser;

    public InterfaceClient(String username) {

        text=new TextArea(10,60);
        text.setEditable(false);
        text.setForeground(Color.red);
        frame.add(text);

        data=new TextField(60);
        frame.add(data);

        /////////////////////
        listUser=new JList<>();
        this.setTitle("Chat Rmi");
        this.username = username ;
        this.idUser = Math.random() + "" + this.username ;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 609, 389);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));


        try {// set icon giao dien---------------------------
            Image iconmes = ImageIO.read(new File( "src/main/resources/logoUser.png"));
            this.setIconImage(iconmes);
        } catch (IOException e1) {
            // TODO Auto-generated catch block

        }

        JPanel panelUsers = new JPanel();
        panelUsers.setPreferredSize(new Dimension(110,200));
        contentPane.add(panelUsers, BorderLayout.WEST);

        listUser.updateUI();
        listUser.setCellRenderer(new UserCell());

        panelUsers.setLayout(new BorderLayout(100, 100));



        JScrollPane scrollPane = new JScrollPane(listUser);
        panelUsers.add(scrollPane);

        panelChat = new JPanel();
        contentPane.add(panelChat, BorderLayout.CENTER);
        panelChat.setLayout(card);

        panelChat.add(text);

        panelinFor = new JPanel();
        panelinFor.setBorder(new LineBorder(new Color(0, 0, 0)));
        contentPane.add(panelinFor, BorderLayout.NORTH);
        panelinFor.setLayout(new BorderLayout(0, 0));

        panel_1 = new JPanel();
        panelinFor.add(panel_1, BorderLayout.WEST);
        panel_1.setLayout(new GridLayout(1, 0, 0, 0));

        lblogoUser = new JLabel("");
        panel_1.add(lblogoUser);
        try {
            BufferedImage bufferImage_user = ImageIO.read(new File("src/main/resources/logoUser.png"));
            ImageIcon imageIcon_user = new ImageIcon(bufferImage_user.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            lblogoUser.setIcon(imageIcon_user);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        labelCurrentUser = new JLabel(this.username);
        panel_1.add(labelCurrentUser);

        panelQuitter = new JPanel();
        panelinFor.add(panelQuitter, BorderLayout.EAST);
        //panelCreateGroup.setLayout(new GridLayout(3,2, 5,10));




        quiter = new JButton("Quitter");
        quiter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panelQuitter.add(quiter);


    }

    public void ecrire(String msg) {
        try {
            this.text.append(msg+"\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }





    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    String s = JOptionPane.showInputDialog(null, "Type your name") ;

                    InterfaceClient frame = new InterfaceClient(s);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class UserCell implements ListCellRenderer{
    //Constant constant = new Constant() ;

    @Override
    public JPanel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
        // TODO Auto-generated method stub

        JPanel panel = new JPanel();
       
        JLabel lb = new JLabel("") ;


        panel.setLayout(new BorderLayout());
//        PacketUser packet = (PacketUser) value;
//        panel.add(packet.getLblUser(), BorderLayout.CENTER);
        panel.add(lb, BorderLayout.WEST) ;
        try {

//                BufferedImage bufferImage_user = ImageIO.read(new File("src/main/resources/logoUser.png"));
//
//                ImageIcon imageIcon_user = new ImageIcon(bufferImage_user.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
//                lb.setIcon(imageIcon_user);



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (isSelected) {
            panel.setBackground(new Color(65, 166, 248));
            ///packet.getLblUser().setForeground(Color.black);
        } else { // when don't select

        }
        return panel;
    }


}



        class quiterListener implements ActionListener {
            InterfaceClient  irc;
            public quiterListener (InterfaceClient  i) {
                irc = i;
            }
            public void actionPerformed (ActionEvent e) {
                try{
                    irc.frm.quiter(irc.id);

                }catch( RemoteException ex){
                    ex.printStackTrace();
                }
            }
        }


        class connecterListener implements ActionListener {
            InterfaceClient  irc;
            public connecterListener (InterfaceClient  i) {
                irc = i;
            }
            public void actionPerformed (ActionEvent e) {
                try {
                    System.out.println("//"+irc.data.getText()+"/IRCServer");
                    Forum server = (Forum) Naming.lookup("//"+irc.data.getText()+"/IRCServer");
                    irc.frm=server;
                    Proxy c=new ProxyImpl(irc);
                    irc.id=irc.frm.entrer(c);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        class quiListener implements ActionListener {
            InterfaceClient  irc;
            public quiListener (InterfaceClient  i) {
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

        class direListener implements ActionListener {
            InterfaceClient  irc;
            public direListener (InterfaceClient i) {
                irc = i;
            }
            public void actionPerformed (ActionEvent e) {
                try{
                    irc.frm.dire(irc.id,irc.data.getText());
                }catch( RemoteException ex){
                    ex.printStackTrace();
                }
            }
        }
