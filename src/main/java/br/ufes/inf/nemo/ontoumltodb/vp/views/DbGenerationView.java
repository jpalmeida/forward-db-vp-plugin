/**
 * This graphical interface was developed using the Windows Builder 1.9.5 plugin for Eclipse.
 * If you have the plugin installed and the interface does not open for editing in the graphic module, 
 * right-click on the class name; select open with; select WindowsBulder Editor
 * 
 * @author 
 *
 */

package br.ufes.inf.nemo.ontoumltodb.vp.views;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;
import br.ufes.inf.nemo.ontoumltodb.vp.settings.ProjectConfigurations;

import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class DbGenerationView extends JPanel {

	private static final long serialVersionUID = 1L;
	private final ButtonGroup mappingStrategyGroup = new ButtonGroup();
	private final ButtonGroup inheritedAttributesGroup = new ButtonGroup();
	private JButton jbtnOk;
	private JButton jbtnCancel;
	private JCheckBox cxbGenerateObda;
	private JTextField jtfBaseIRI;
	private JTextField jtfHost;
	private JTextField jtfDatabase;
	private JTextField jtfUser;
	private JTextField jtfPassword;
	private JCheckBox cxbCreateIndex;
	private JCheckBox cxbGenerateSchema;
	private JCheckBox cxbGenerateConnection;
	private JCheckBox cbxStandarizeNames;
	private JCheckBox cxbEnumToLookupTable;
	private JCheckBox cbxGenerateViews;
	private JCheckBox cxbGenerateTrigger;
	private JComboBox<String> cbxDBMS;
	private JPanel panel_6;
	private JRadioButton rdbtnInheritedAttributes;
	private JRadioButton rdbtnDirectAttributes;
	private JPanel panel_4;
	private JRadioButton rbtOneTablePerClass;
	private JRadioButton rbtOneTablePerConcreteClass;
	private JRadioButton rbtOneTablePerKind;
	
	/**
	 * Create the panel.
	 */
	public DbGenerationView(ProjectConfigurations configurations) {
		setPreferredSize(new Dimension(442, 326));
		setMinimumSize(new Dimension(500, 360));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Transformations", null, panel_5, null);
		
		cxbGenerateSchema = new JCheckBox("Generate script");
		
		JLabel lblNewLabel = new JLabel("DBMS");
		
		cbxDBMS = new JComboBox<String>();
		cbxDBMS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableCreateIndex();
				enableEnumToLookupTable();
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("eg.: 'BirthDate' to 'birth_date'");
		
		cxbEnumToLookupTable = new JCheckBox("Enum column to lookup table ");
		
		cbxStandarizeNames = new JCheckBox("Standardize database names");
		
		cxbCreateIndex = new JCheckBox("Create indexes");
		
		cxbGenerateTrigger = new JCheckBox("Create triggers");
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Mapping Strategy", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		rbtOneTablePerClass = new JRadioButton("One Table per Class");
		mappingStrategyGroup.add(rbtOneTablePerClass);
		
		rbtOneTablePerConcreteClass = new JRadioButton("One Table per Concrete Class");
		mappingStrategyGroup.add(rbtOneTablePerConcreteClass);
		
		rbtOneTablePerKind = new JRadioButton("One Table per Kind");
		mappingStrategyGroup.add(rbtOneTablePerKind);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(rbtOneTablePerClass)
						.addComponent(rbtOneTablePerConcreteClass)
						.addComponent(rbtOneTablePerKind))
					.addContainerGap(33, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(rbtOneTablePerClass)
					.addPreferredGap(ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
					.addComponent(rbtOneTablePerConcreteClass)
					.addGap(43)
					.addComponent(rbtOneTablePerKind)
					.addGap(29))
		);
		panel_4.setLayout(gl_panel_4);
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cxbGenerateSchema))
						.addGroup(gl_panel_5.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbxDBMS, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_5.createSequentialGroup()
									.addComponent(cxbGenerateTrigger)
									.addGap(66))
								.addComponent(cxbEnumToLookupTable)
								.addGroup(gl_panel_5.createSequentialGroup()
									.addGap(21)
									.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
								.addComponent(cbxStandarizeNames)
								.addComponent(cxbCreateIndex))))
					.addContainerGap(105, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(cxbGenerateSchema)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbxDBMS, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(cxbEnumToLookupTable)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbxStandarizeNames)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cxbCreateIndex)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cxbGenerateTrigger))
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(197, Short.MAX_VALUE))
		);
		panel_5.setLayout(gl_panel_5);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("OBDA", null, panel_2, null);
		
		cxbGenerateObda = new JCheckBox("Generate OBDA file");
		
		JLabel lblNewLabel_2 = new JLabel("Base IRI:");
		
		jtfBaseIRI = new JTextField();
		jtfBaseIRI.setText("https://example.com");
		jtfBaseIRI.setColumns(10);
		
		cxbGenerateConnection = new JCheckBox("Generate connection from Proteg\u00E9");
		
		JLabel lblNewLabel_3 = new JLabel("Host:");
		
		jtfHost = new JTextField();
		jtfHost.setText((String) null);
		jtfHost.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Database:");
		
		jtfDatabase = new JTextField();
		jtfDatabase.setText((String) null);
		jtfDatabase.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("User:");
		
		jtfUser = new JTextField();
		jtfUser.setText((String) null);
		jtfUser.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Password:");
		
		jtfPassword = new JTextField();
		jtfPassword.setText((String) null);
		jtfPassword.setColumns(10);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(cxbGenerateConnection)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblNewLabel_2)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jtfBaseIRI, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
						.addComponent(cxbGenerateObda)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(jtfHost, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
								.addComponent(jtfDatabase, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
								.addComponent(jtfUser, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jtfPassword, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(cxbGenerateObda)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(jtfBaseIRI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(cxbGenerateConnection)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(jtfHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_4)
						.addComponent(jtfDatabase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_5)
						.addComponent(jtfUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_6)
						.addComponent(jtfPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Views", null, panel_3, null);
		
		cbxGenerateViews = new JCheckBox("Generate Views");
		
		panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Create views with attributes:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbxGenerateViews))
					.addContainerGap(197, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(cbxGenerateViews)
					.addGap(18)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(75, Short.MAX_VALUE))
		);
		
		rdbtnDirectAttributes = new JRadioButton("created in the class");
		inheritedAttributesGroup.add(rdbtnDirectAttributes);
		
		rdbtnInheritedAttributes = new JRadioButton("inherited from the superclasses");
		inheritedAttributesGroup.add(rdbtnInheritedAttributes);
		
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnInheritedAttributes)
						.addComponent(rdbtnDirectAttributes))
					.addContainerGap(60, Short.MAX_VALUE))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnDirectAttributes)
					.addGap(18)
					.addComponent(rdbtnInheritedAttributes)
					.addContainerGap(53, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);
		panel_3.setLayout(gl_panel_3);
		
		jbtnOk = new JButton("Do Mapping");
		jbtnOk.setMaximumSize(new Dimension(135, 29));
		jbtnOk.setMinimumSize(new Dimension(135, 29));
		jbtnOk.setPreferredSize(new Dimension(135, 29));
		
		jbtnCancel = new JButton("Cancel");
		jbtnCancel.setMaximumSize(new Dimension(135, 29));
		jbtnCancel.setMinimumSize(new Dimension(135, 29));
		jbtnCancel.setPreferredSize(new Dimension(135, 29));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(41)
					.addComponent(jbtnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
					.addComponent(jbtnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(46))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(14, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jbtnOk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jbtnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);
		
		cbxDBMS.setModel(new DefaultComboBoxModel(DbmsSupported.values()));
		
		setComponentsValues(configurations);
	}

	private void enableCreateIndex() {
		if (rbtOneTablePerKind.isSelected()) {
			cxbCreateIndex.setEnabled(true);
		} else {
			cxbCreateIndex.setSelected(false);
			cxbCreateIndex.setEnabled(false);
		}
	}

	/**
	 * Updates components with project configurations' information.
	 *
	 * @param configurations
	 */
	private void setComponentsValues(ProjectConfigurations configurations) {
		cxbGenerateSchema.setSelected(configurations.isGenerateSchema());
		cxbGenerateTrigger.setSelected(configurations.isGenerateTrigger());
		cxbGenerateObda.setSelected(configurations.isGenerateObda());
		cxbGenerateConnection.setSelected(configurations.isGenerateConnection());
		cbxGenerateViews.setSelected(configurations.isGenerateViews());

		if (configurations.getMappingStrategy() != null) {
			if (configurations.getMappingStrategy() == MappingStrategy.ONE_TABLE_PER_CLASS)
				rbtOneTablePerClass.setSelected(true);
			else if (configurations.getMappingStrategy() == MappingStrategy.ONE_TABLE_PER_CONCRETE_CLASS)
				rbtOneTablePerConcreteClass.setSelected(true);
			else
				rbtOneTablePerKind.setSelected(true);
		}
		
		cbxDBMS.setSelectedItem(configurations.getTargetDBMS());
		cxbEnumToLookupTable.setSelected(configurations.isEnumFieldToLookupTable());
		cbxStandarizeNames.setSelected(configurations.isStandardizeNames());
		cxbCreateIndex.setSelected(configurations.isGenerateIndex());
		jtfBaseIRI.setText(configurations.getBaseIri());
		jtfHost.setText(configurations.getHostNameConnection());
		jtfDatabase.setText(configurations.getDatabaseNameConnection());
		jtfUser.setText(configurations.getUserNameConnection());
		jtfPassword.setText(configurations.getPasswordConnection());
		
		if(configurations.isInheritedAttributes())
			rdbtnInheritedAttributes.setSelected(true);
		else rdbtnDirectAttributes.setSelected(true);
		
		enableEnumToLookupTable();
		enableCreateIndex();
	}

	/** Updates project configurations with components' information. */
	public void updateConfigurationsValues(ProjectConfigurations configurations) {
		configurations.setGenerateSchema(cxbGenerateSchema.isSelected());
		configurations.setGenerateTrigger(cxbGenerateTrigger.isSelected());
		configurations.setGenerateObda(cxbGenerateObda.isSelected());
		configurations.setGenerateConnection(cxbGenerateConnection.isSelected());
		configurations.setGenerateViews(cbxGenerateViews.isSelected());
		
		if (rbtOneTablePerClass.isSelected())
			configurations.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_CLASS);
		else if (rbtOneTablePerConcreteClass.isSelected())
			configurations.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_CONCRETE_CLASS);
		else
			configurations.setMappingStrategy(MappingStrategy.ONE_TABLE_PER_KIND);

		configurations.setTargetDBMS((DbmsSupported) cbxDBMS.getSelectedItem());
		configurations.setEnumFieldToLookupTable(cxbEnumToLookupTable.isSelected());
		configurations.setStandardizeNames(cbxStandarizeNames.isSelected());
		configurations.setGenerateIndexes(cxbCreateIndex.isSelected());
		configurations.setBaseIri(jtfBaseIRI.getText());
		configurations.setHostNameConnection(jtfHost.getText().trim());
		configurations.setDatabaseNameConnection(jtfDatabase.getText().trim());
		configurations.setUserNameConnection(jtfUser.getText().trim());
		configurations.setPasswordConnection(jtfPassword.getText().trim());
		configurations.setInheritedAttributes(rdbtnInheritedAttributes.isSelected());
	}

	public void onExport(ActionListener onExportAction) {
		ActionListener[] currentListeners = jbtnOk.getActionListeners();

		for (int i = 0; currentListeners != null && i < currentListeners.length; i++) {
			jbtnOk.removeActionListener(currentListeners[i]);
		}

		jbtnOk.addActionListener(onExportAction);
	}

	public void onCancel(ActionListener onCancelAction) {
		ActionListener[] currentListeners = jbtnCancel.getActionListeners();

		for (int i = 0; currentListeners != null && i < currentListeners.length; i++) {
			jbtnCancel.removeActionListener(currentListeners[i]);
		}

		jbtnCancel.addActionListener(onCancelAction);
	}

	public boolean checkValidParamters() {
		if ((DbmsSupported) cbxDBMS.getSelectedItem() == DbmsSupported.GENERIC_SCHEMA
				&& !cxbEnumToLookupTable.isSelected()) {
			JOptionPane.showMessageDialog(this, "You can not create an enumeration field to a generic database.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!cxbGenerateSchema.isSelected() && !cxbGenerateObda.isSelected() && !cxbGenerateConnection.isSelected()  && 
				!cxbGenerateTrigger.isSelected() && cxbCreateIndex.isSelected()) {
			JOptionPane.showMessageDialog(this,
					"You must generate something.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
	
	private void enableEnumToLookupTable() {
		if( cbxDBMS.getSelectedItem() == DbmsSupported.GENERIC_SCHEMA) {
			cxbEnumToLookupTable.setSelected(true);
			cxbEnumToLookupTable.setEnabled(false);
		}
		else {
			cxbEnumToLookupTable.setEnabled(true);
		}
	}
}
