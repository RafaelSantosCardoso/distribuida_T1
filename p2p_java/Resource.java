public class Resource {
    private String resourceName;
	private String hash;
	private Peer peer;

    public Resource(String resourceName, String hash, Peer peer){
        this.resourceName = resourceName;
        this.hash = hash;
        this.peer = peer;
    }

    public String getResourceName(){
        return this.resourceName;
    }
    public String getHash(){
        return this.hash;
    } 
    public Peer getPeer(){
        return this.peer;
    } 

    public void setResourceName(String resourceName){
        this.resourceName = resourceName;
    }
    public void setHash(String hash){
        this.hash = hash;
    } 
    public void setPeer(Peer peer){
        this.peer = peer;
    } 
    @Override
    public String toString() {
        return ("\n#########################\n Hash do Recurso: "+ hash +"\nName do Recurso: "+resourceName + peer.toString());
    }
}
