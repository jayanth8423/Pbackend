package klu;


@javax.persistence.Entity
public class Items {
    @javax.persistence.Id
    private int pid;
    private String pname;
    private String pimg;
    private float pprs;
    private String pcategory;
    private int quantity;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public float getPprs() {
        return pprs;
    }

    public void setPprs(float pprs) {
        this.pprs = pprs;
    }

    public String getPcategory() {
        return pcategory;
    }

    public void setPcategory(String pcategory) {
        this.pcategory = pcategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Items [pid=" + pid + ", pname=" + pname + ", pimg=" + pimg + ", pprs=" + pprs + ", pcategory=" + pcategory + ", quantity=" + quantity + "]";
    }
}
 