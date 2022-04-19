public class Peer {
    private String id;
    private String name;
	private String addrIp;
	private Integer port;
	private Integer timeout = 0;

    public Peer(String id, String name, String addrIp, Integer port){
        this.id = id;
        this.name = name;
        this.addrIp = addrIp;
        this.port = port;
    }

    public String getName(){
        return this.name;
    }
    public String getId(){
        return this.id;
    }
    public String getAddrIp(){
        return this.addrIp;
    } 
    public Integer getPort(){
        return this.port;
    } 
    public Integer getTimeout(){
        return this.timeout;
    } 
    
    public void setName(String name){
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }
    public void getAddrIp(String addrIp){
        this.addrIp = addrIp;
    } 
    public void setPort(Integer port){
        this.port = port;
    } 
    public void setTimeout(Integer timeout){
        this.timeout = timeout;
    } 
    @Override
    public String toString() {
        return ("\n#########################\nNome do Peer: "+name +"\nId do Peer: "+ id +"\nIP do Peer: "+ addrIp +"\nPorta do Peer: "+ port );
    }
}
