import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;

public class Server implements ServerInterface {

    //                      IP      HashFile
    private static HashMap<Integer, Integer> hash = new HashMap();

    @Override
    public int register (Integer ip) {
        try {
            remoteHostName = getClientHost();
            hashConnections.put(registred, "rmi://" + remoteHostName + ":" + port + "/client");
        } catch (Exception e) {
            System.out.println("Registration failed");
            e.printStackTrace();
        }
        registred++;
        return registred - 1;
    }

    @Override
    public int end(int id) throws RemoteException {
        return 0;
    }

}
