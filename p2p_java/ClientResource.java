public class ClientResource {
    private String resourceName;
	private String hash;

    public ClientResource(String resourceName, String hash){
        this.resourceName = resourceName;
        this.hash = hash;
    }

    public String getResourceName(){
        return this.resourceName;
    }
    public String getHash(){
        return this.hash;
    } 

    public void setResourceName(String resourceName){
        this.resourceName = resourceName;
    }
    public void setHash(String hash){
        this.hash = hash;
    } 

    @Override
    public String toString() {
        return ("\n#########################\nHash do Recurso: "+ hash +"\nName do Recurso: "+resourceName);
    }
}