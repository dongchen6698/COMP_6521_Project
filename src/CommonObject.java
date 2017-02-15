/**
 * Created by AlexChen on 2017-02-12.
 */
public class CommonObject implements Comparable<CommonObject>{
    private String EmpID;
    private String Content;

    public CommonObject(String line) {

        this.EmpID = line.substring(0,7);
        this.Content = line;
    }

    public String getEmpID() {
        return EmpID;
    }

    @Override
    public String toString() {

        return this.Content;
    }

    public int compareTo(CommonObject compareEmpID) {

        int compareID = Integer.parseInt((compareEmpID).getEmpID());

        //ascending order
        return Integer.parseInt(this.EmpID) - compareID;
    }

}

