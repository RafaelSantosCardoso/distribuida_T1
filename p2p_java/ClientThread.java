public class ClientThread extends Thread {
    protected String[] thread_args;
    protected String thread_name;

    public ClientThread(String[] args, String name) {
        thread_args = args;
        thread_name = name;
    }