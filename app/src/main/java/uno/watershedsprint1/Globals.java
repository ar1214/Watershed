package uno.watershedsprint1;

//singleton class used for Global variables, token and projectID, basic get and set
//example usage:
//Globals g = Globals.getInstance();
//g.getToken();

public class Globals {
    private static Globals instance;

    private String token;

    private int projId;

    private Globals(){}

    public void setToken(String s){
        this.token = s;
    }

    public String getToken(){
        return this.token;
    }

    public void setId(int i){this.projId = i;}

    public int getId(){return this.projId;}

    public static synchronized Globals getInstance(){
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

}
