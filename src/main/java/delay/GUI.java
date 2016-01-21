package delay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import gui.DateLabelFormatter;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Container container;
	private JButton go;
	private JPanel east;
	private JPanel west;
	private UtilDateModel date;
	private Properties p;
	private JTable tableau;
	private JDatePickerImpl datePicker;
	private JDatePanelImpl datePanel;
	private JTextArea delay;
	private JTextField busId;
	private JSpinner timeSpinner;
	private JSpinner.DateEditor timeEditor;
	private JScrollPane scroll;
	private final String[] locationsArray = { "ANO VOLIMES", "VOLIMES", "ORTHONIES", "ANAFONITRIA", "ALIKANAS",
			"KATASTARI", "ANO GERAKARIO", "MESO GERAKARIO", "MARIES", "PIGADAKIA", "KYPSELI", "PLANOS", "TRAGAKI",
			"KATO GERAKARIO", "KALITHEA", "EXOHORA", "GYRI", "KALIPADO", "AGIOS DIMITRIOS", "SKOULIKADO", "LOUHA",
			"VANATO", "AGIA MARINA", "BOHALI", "AGERIKOS", "ΓΑΙΤΑΝΙ", "SARAKINADO", "ZAKYNTHOS", "GALARO",
			"AGIOI PANTES", "FIOLITIS", "AGIOS LEON", "LAGADAKIA", "VOUGIATO", "AMPELOKIPOI", "MAXAIRADO", "ARGASI",
			"LAGOPODO", "KOILIWMENO", "ROMIRI", "KALAMAKI", "MOUZAKI", "PANTOKRATORAS", "LITHAKIA", "VASILIKOS",
			"AGALAS", "KERI", "DRAKAS", "KABI", "AGIOS SOSTIS", "AIRPORT", "ALYKES", "LAGANAS", "PORTO KOUKLA",
			"SKINARI", "TSILIVI", "NAVAGIO", "AMOUDI", "ASKOS", "XIROKASTELLO", "AKROTIRI", "GERAKAS", "DROSIA",
			"MARINEIKA", "PORTO", "5o DIMOTIKO", "ATHENS", "ΑΥΡΙΑΚΟΣ", "KERI", "PORT FERRY", "PSAROU", "KALPAKI",
			"PARKING", "KORITHI", "AGIOS NIKOLAOS", "HARTATA", "XYGIA", "VOLOS", "IOANNINA", "ARTA", "AGRINIO", "PATRA",
			"PIRGOS", "KORINTHOS", "CHALKIDA", "THESSALONIKI", "LARISA", "VEROIA", "KASTORIA", "METSOVO", "AIGIO",
			"LAMIA", "KARPENISSI", "DELFI", "ARGOSTOLI", "SAMI", "POROS", "PESADA", "TRIPOLI", "KALAMATA", "SPARTI",
			"GITHIO", "PILOS", "NAFPLIO", "KAVALA", "KOMOTINI", "ALEXANDROUPOLI", "ALEXANDROUPOLI", "THIVA", "TRIKALA",
			"KARDITSA", "KATERINI", "KERKIRA", "OLYMPIA", "KALAVRITA", "KIATO", "XILOKASTRO", "AKRATA", "MEGARA",
			"KILINI", "SERRES", "MESOLONGI", "NAFPAKTOS", "LEFKADA", "PREVEZA", "KALAMPAKA" };
	private JComboBox<String> locations;

	public GUI() {

		// Modifier le titre
		this.setTitle("Delay");

		// Taille et Modif
		this.setSize(900, 600);
		this.setResizable(true);

		// instanciation
		this.go = new JButton("Search");
		this.east = new JPanel();
		this.west = new JPanel();
		this.busId = new JTextField("busId");

		// JdatePicker
		this.date = new UtilDateModel();
		this.date.setDate(2015, 10, 5);
		this.date.setSelected(true);

		// poperties for JDatePanelImpl
		this.p = new Properties();
		this.p.put("text.today", "Today");
		this.p.put("text.month", "Month");
		this.p.put("text.year", "Year");
		this.datePanel = new JDatePanelImpl(date, p);
		this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		this.timeSpinner = new JSpinner(new SpinnerDateModel());
		this.timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		this.timeSpinner.setEditor(timeEditor);
		this.timeSpinner.setValue(new Date()); // will only show the current
		this.delay = new JTextArea("Delay");
		
		// time
		this.locations = new JComboBox<String>(this.locationsArray);

		// Panel west
		this.west.setLayout(new GridLayout(5, 1));
		this.west.add(this.busId);
		this.west.add(this.timeSpinner);
		this.west.add(this.datePicker);
		this.west.add(this.locations);
		this.west.add(this.go);
		this.west.add(this.delay);
		this.west.setVisible(true);

		// table & centre
		this.east.setLayout(new BorderLayout());
		String[][] donnees = {};
		// String String hashMap int TRavelLEgs Long
		final String[] entetes = { "BusId", "Start", "End", "Stops"};
		final TableDonnee tablemodel = new TableDonnee(donnees, entetes);
		this.tableau = new JTable(tablemodel);
		this.east.add(tableau.getTableHeader(), BorderLayout.NORTH);
		this.east.add(tableau, BorderLayout.EAST);
		//this.east.setSize(500, 500);
		this.east.setBackground(Color.BLACK);
		this.east.setVisible(true);

		// search
		this.go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				LocalDate selectedDate = ((Date) datePicker.getModel().getValue()).toInstant()
						.atZone(ZoneId.of("Europe/Athens")).toLocalDate();
				Date tmpe = (Date) timeSpinner.getValue();
				Instant instant = Instant.ofEpochMilli(tmpe.getTime());
				LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
				LocalDateTime currentTime = selectedDate.atTime(res);
				String[][] donnees = new String[0][14];
				tablemodel.setDataVector(donnees, entetes);
				
				DelayProcess delayObject = new DelayProcess(currentTime.toString(), null, busId.getText(), (String)locations.getSelectedItem());
				delayObject.process();
				delay.setText(delayObject.getDelay().toHours()+"H "+(delayObject.getDelay().toMinutes()%60)+"M "+(delayObject.getDelay().getSeconds()%60)+"S");
				
			}
		});
		;

		// corps
		this.setLayout(new BorderLayout());
		this.container = this.getContentPane();
		this.container.add(this.west, BorderLayout.WEST);
		this.scroll = new JScrollPane(this.east);
		this.container.add(scroll, BorderLayout.EAST);

		// Exit et Pop
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(50, 50);
		// this.pack();

	}

	class TableDonnee extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[][] donnee;
		private String[] title;

		public TableDonnee(String[][] donnee, String[] title) {
			this.donnee = donnee;
			this.title = title;
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
