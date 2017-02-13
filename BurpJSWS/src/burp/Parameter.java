package burp;

/**
 * Created by ken on 3/11/16.
 */
public class Parameter {
    private String pName;
    private String pType;

    public Parameter(String pName, String pType) {
        this.pName = pName;
        this.pType = pType;
    }

    public Parameter(String pName) {
        this.pName = pName;
    }

    public String getpName() {
        return pName;
    }

    public boolean knowType()
    {
        return pType != null && pType != "";
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    @Override
    public String toString() {

        String ret= "Parameter{" +
                "pName='" + pName + '\'' +
                ", pType='" ;
        ret += pType == null ? "unknown" : pType ;
        ret += "'}";
        return ret;
    }
}
