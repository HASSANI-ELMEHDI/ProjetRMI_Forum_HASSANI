import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

public class ForumImpl extends UnicastRemoteObject implements Forum {
         int i;//id d'un client;
        String hostname;
        TreeMap<Integer,Proxy> ListClient;
        public ForumImpl() throws RemoteException {
            super();
            i=0;
            ListClient= new TreeMap<Integer,Proxy>();

            try{
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public String qui() throws RemoteException {

                    dire(0,ListClient.keySet().toString()+"\n");

            return "";
        }

        public int entrer(Proxy cb) throws RemoteException{
            i++;

            cb.ecouter("Bienvenue,Vous etes connecté ( id ="+i+")");
            ListClient.put(i,cb);
            qui();

            return i;
        }

        public void dire(int id, String msg) throws RemoteException{


            ListClient.forEach((k,v)->{
                try {

                   v.ecouter(id+":"+msg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public void quiter(int id) throws RemoteException{
            Proxy cb;
            cb=ListClient.get(id);
            ListClient.remove(id);
            cb.ecouter("Vous etes deconnecté ");
            qui();
        }


        public static void main(String args[]) {
            try {
                Registry registry = LocateRegistry.createRegistry(1099);
                Forum server = new ForumImpl();
                String hostname = InetAddress.getLocalHost().getHostName();
                Naming.rebind("//"+hostname+"/Forum", server);
                System.out.println("Server running ...");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }