package uno.watershedsprint1;


public class Globals {
    private static Globals instance;

    private String token;

    private Globals(){}

    public void setToken(String s){
        this.token = s;
    }

    public String getToken(){
        return this.token;
    }

    public static synchronized Globals getInstance(){
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

}
