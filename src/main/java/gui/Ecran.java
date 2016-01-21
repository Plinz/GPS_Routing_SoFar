package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import database.StatisticsQuery;
import gps_log_processor.GestionTimePeriod;
import gps_log_processor.LegCollection;

public class Ecran extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Container container;
	private JButton go;
	private JButton export;
	private JButton saveBDD;
	private JPanel nord;
	private JPanel centre;
	private int hfrom;
	private int hto;
	private UtilDateModel model;
	private UtilDateModel model2;
	private Properties p;
	private JTable tableau;
	private JDatePickerImpl datePicker;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker2;
	private JDatePanelImpl datePanel2;
	private final String[] heures= {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	private JComboBox<String> heuref;
	private JComboBox<String> heuret;
	private JCheckBox cutByMonth;
	private GestionTimePeriod td;
	private JTextField tag;
	private JScrollPane scroll;
	private String tagcsv;
	private List<LegCollection> currentList;
	
	public Ecran(){
		
		//Modifier le titre
			this.setTitle("Analize");
		
		//Taille et Modif
			this.setSize(900,600);
			this.setResizable(true);
		
		//instanciation
			this.go = new JButton("Search");
			this.export = new JButton("Export CSV");
			this.saveBDD = new JButton("Save in BDD");
			this.export.setEnabled(false);
			this.saveBDD.setEnabled(false);
			this.nord = new JPanel();
			this.centre = new JPanel();
			this.heuref = new JComboBox<String>(heures);
			this.heuret = new JComboBox<String>(heures);
			this.tag = new JTextField("Tag");
			this.cutByMonth = new JCheckBox("By Month");
			this.tagcsv = "";
			
			
		//JdatePicker
			this.model = new UtilDateModel();
			this.model.setDate(2015, 10, 5);
			this.model.setSelected(true);
			
			this.model2 = new UtilDateModel();
			this.model2.setDate(2015, 10, 5);
			this.model2.setSelected(true);
			
		//poperties for JDatePanelImpl
			this.p = new Properties();
			this.p.put("text.today", "Today");
			this.p.put("text.month", "Month");
			this.p.put("text.year", "Year");
			this.datePanel = new JDatePanelImpl(model, p);
			this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
			
			this.datePanel2 = new JDatePanelImpl(model2, p);
			this.datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
			
		//Panel nord
			this.nord.setSize(900,100);
			this.nord.setLayout(new GridLayout(1, 18));
			
			this.nord.add(tag);
			this.nord.add(datePicker);
			this.nord.add(heuref);
			this.nord.add(datePicker2);
			this.nord.add(heuret);
			this.nord.add(cutByMonth);
			this.nord.add(go);
			this.nord.add(export);
			this.nord.add(saveBDD);
			
			this.nord.setVisible(true);
		
		//Combo box
			this.heuref.addActionListener (new ActionListener () {
			    @SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
			    	heuref = ((JComboBox<String>)e.getSource());
			        String tmp = (String)heuref.getSelectedItem();
			        hfrom = Integer.parseInt(tmp);
			    }
			});
			this.heuret.addActionListener (new ActionListener () {
			    @SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
			    	 heuret = (JComboBox<String>)e.getSource();
			         String tmp2 = (String)heuret.getSelectedItem();
			         hto = Integer.parseInt(tmp2);
			    }
			});
				
		//table & centre 
			this.centre.setLayout(new BorderLayout());
			String[][] donnees ={};	
		    //                  String    String   hashMap      int           TRavelLEgs  Long
		    final String[] entetes = {"Tag","Month","Year","PointA","PointB","Stop 1-5","Stop 5-10","Stop > 10 ","AvgTime","SqrtTime","AvgDistance","Occurence","MinTime","MaxTime",};
		    final TableDonnee tablemodel = new TableDonnee(donnees,entetes);
		    this.tableau = new JTable(tablemodel);
		    this.centre.add(tableau.getTableHeader(), BorderLayout.NORTH);
		    this.centre.add(tableau, BorderLayout.CENTER);
		    this.centre.setSize(500,500);
			this.centre.setBackground(Color.BLACK);
			this.centre.setVisible(true);

			//Save in BDD
			this.saveBDD.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cutByMonth.isSelected()){
						StatisticsQuery query = new StatisticsQuery();
						query.insertAll(currentList);
					}
				}
			});
			
			// search
			this.go.addActionListener(new ActionListener()
			{
			    public void actionPerformed(ActionEvent e)
			    {

			    	LocalDate selectedDate = ((Date)datePicker.getModel().getValue()).toInstant().atZone(ZoneId.of("Europe/Athens")).toLocalDate();
			    	LocalDate selectedDate2 = ((Date)datePicker2.getModel().getValue()).toInstant().atZone(ZoneId.of("Europe/Athens")).toLocalDate();
				    
			    	System.out.println("Date : "+selectedDate+"\n"+
				    					"from : "+hfrom+"\n"+
				    					"Date : "+selectedDate2+"\n"+
				    					"to : "+hto+"\n");
				    LocalDateTime fromtmp = selectedDate.atStartOfDay().plusHours(hfrom);
				    LocalDateTime totmp = selectedDate2.atStartOfDay().plusHours(hto);
				    td = new GestionTimePeriod(fromtmp,totmp);
				    String[][] donnees = new String[0][14];
				    String[] rowData = new String[14];
				    tablemodel.setDataVector(donnees, entetes);
				    if (cutByMonth.isSelected())
				    	currentList = td.statsCutByMonth();
				    else
				    	currentList = td.NormalStats();

				    for(LegCollection lc : currentList){
						lc.statProcess();
				    	lc.setTag(tag.getText());
					}
				    	
					for(int i = 0; i < currentList.size() ; i++ ){
						rowData[0] = currentList.get(i).getTag();
						rowData[1] = currentList.get(i).getMonth();
						rowData[2] = currentList.get(i).getYear()+"";
						rowData[3] = currentList.get(i).getPointA().getName();
					   	rowData[4] = currentList.get(i).getPointB().getName();
					   	rowData[5] = currentList.get(i).getAvgStopsByTime().get(Duration.ofMinutes(1))+"";
					   	rowData[6] = currentList.get(i).getAvgStopsByTime().get(Duration.ofMinutes(5))+"";
					   	rowData[7] = currentList.get(i).getAvgStopsByTime().get(Duration.ofMinutes(10))+"";
					   	Duration tmp =currentList.get(i).getAvgTravelTime();
					  	rowData[8] = ""+(int)tmp.toHours()+"h "+(int)tmp.toMinutes()%60+"m "+(int)tmp.getSeconds()%60+"s";
					   	Duration tmp2 = currentList.get(i).getSqrtAvgTravelTime();
					   	rowData[9] = ""+(int)tmp2.toHours()+"h "+(int)tmp2.toMinutes()%60+"m "+(int)tmp2.getSeconds()%60+"s";
					   	rowData[10] = ((int)currentList.get(i).getAvgDistance())+"";
					   	rowData[11] = currentList.get(i).getLogedLegs().size()+"";
					   	Duration tmp3 = currentList.get(i).getMinTravelTime();
					   	rowData[12] = ""+(int)tmp3.toHours()+"h "+(int)tmp3.toMinutes()%60+"m "+(int)tmp3.getSeconds()%60+"s";
					  	Duration tmp4 = currentList.get(i).getMaxTravelTime();
					   	rowData[13] = ""+(int)tmp4.toHours()+"h "+(int)tmp4.toMinutes()%60+"m "+(int)tmp4.getSeconds()%60+"s";
					   	tablemodel.addRow(rowData);
					}
					if (tablemodel.getRowCount()!=0){
						saveBDD.setEnabled(true);
						export.setEnabled(true);
					}
			    }
			});;
			
			
			//exporter
			this.export.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					tagcsv = tag.getText();
					try {
						Exporter exp = new Exporter();
						exp.exportTable(tableau, new File(tagcsv+".csv"));
					} catch (IOException ex) {
						System.out.println(ex.getMessage());
						ex.printStackTrace();
					}
				}
			});;
		
		//corps
			this.setLayout(new BorderLayout());
			this.container = this.getContentPane();
			this.container.add(nord,BorderLayout.NORTH);
			this.scroll = new JScrollPane(centre);
			this.container.add(scroll,BorderLayout.CENTER);
		
		
		
		
			
		//Exit et Pop
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocation(50, 50);
			//this.pack();
		
	}
	
	class TableDonnee extends DefaultTableModel{

		private static final long serialVersionUID = 1L;
		private String[][] donnee;
		private String[] title;
		
		public TableDonnee(String[][] donnee, String[] title){
			this.donnee=donnee;
			this.title=title;
		}
		
		public String getColumnName(int col) {
			return this.title[col];
		}
		
	    public int getColumnCount() {
	    	return this.title.length;
	    }

		public String[][] getDonnee() {
			return donnee;
		}

		public void setDonnee(String[][] donnee) {
			this.donnee = donnee;
		}          
	}

}
