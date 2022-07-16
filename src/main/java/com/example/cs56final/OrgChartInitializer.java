package com.example.cs56final;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;

public class OrgChartInitializer
{
    // This method creates an ArrayList of TreeItems (Employees)
    public ArrayList<TreeItem<String>> getEmployees()
    {
        ArrayList<TreeItem<String>> employees = new ArrayList<TreeItem<String>>();

        TreeItem vp1 = new TreeItem("VP, Chris Smith");
        vp1.getChildren().addAll(getVp1s());

        TreeItem vp2 = new TreeItem("VP, Shawn Lloyd");
        vp2.getChildren().addAll(getVp2s());

        employees.add(vp1);
        employees.add(vp2);

        return employees;
    }

    // This method creates an ArrayList of TreeItems (vp1s)
    private ArrayList<TreeItem> getVp1s()
    {
        ArrayList<TreeItem> vp1s = new ArrayList<TreeItem>();

        TreeItem mgr1 = new TreeItem("Manager, Matt Barber");
        mgr1.getChildren().addAll(getMgr1s());
        TreeItem mgr2 = new TreeItem("Manager, Steven Zhang");
        mgr2.getChildren().addAll(getMgr2s());
        TreeItem mgr3 = new TreeItem("Manager, Frank Lozano");
        mgr3.getChildren().addAll(getMgr3s());

        vp1s.add(mgr1);
        vp1s.add(mgr2);
        vp1s.add(mgr3);

        return vp1s;
    }

    // This method creates an ArrayList of TreeItems (vp2s)
    private ArrayList<TreeItem> getVp2s()
    {
        ArrayList<TreeItem> vp2s = new ArrayList<TreeItem>();

        TreeItem mgr1 = new TreeItem("Manager, Glenn Jordan");

        vp2s.add(mgr1);

        return vp2s;
    }

    // This method creates an ArrayList of TreeItems (mgr1s)
    private ArrayList<TreeItem> getMgr1s()
    {
        ArrayList<TreeItem> mgr1s = new ArrayList<TreeItem>();

        TreeItem staff1 = new TreeItem("Senior Analyst, Scott Feng");
        TreeItem staff2 = new TreeItem("Analyst, Shane Kumar");
        TreeItem staff3 = new TreeItem("Accountant, Joshua Weber");

        mgr1s.add(staff1);
        mgr1s.add(staff2);
        mgr1s.add(staff3);

        return mgr1s;
    }

    private ArrayList<TreeItem> getMgr2s()
    {
        ArrayList<TreeItem> mgr2s = new ArrayList<TreeItem>();

        TreeItem staff1 = new TreeItem("Analyst, Alice Kim");
        TreeItem staff2 = new TreeItem("Software Engineer, Nicolas Jordan");

        mgr2s.add(staff1);
        mgr2s.add(staff2);

        return mgr2s;
    }

    private ArrayList<TreeItem> getMgr3s()
    {
        ArrayList<TreeItem> mgr3s = new ArrayList<TreeItem>();

        TreeItem staff1 = new TreeItem("Janitor, Paton McDonald");

        mgr3s.add(staff1);

        return mgr3s;
    }
}