package gui;

import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
public class Exporter  {
    public Exporter() { }
    public void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        FileWriter out = new FileWriter(file);
       
        for(int i=0; i < model.getColumnCount(); i++) {
        	if (i!=5)
        		out.write(model.getColumnName(i) + ";");
        }
        out.write("\n");
        for(int i=0; i< model.getRowCount(); i++) {
            for(int j=0; j < model.getColumnCount(); j++) {
            	if (j!=6){
            		out.write(model.getValueAt(i,j).toString()+";");
            	}
            }
            out.write("\n");
        }
        out.close();
        System.out.println("write out to: " + file);
    }
}