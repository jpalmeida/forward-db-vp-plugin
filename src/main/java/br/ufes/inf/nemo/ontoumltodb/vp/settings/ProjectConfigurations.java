package br.ufes.inf.nemo.ontoumltodb.vp.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;
import br.ufes.inf.nemo.ontoumltodb.util.MappingStrategy;

public class ProjectConfigurations {

	//public static final String DEFAULT_SCHEMA_EXPORT_PATH = System.getProperty("user.home");
	//public static final String DEFAULT_SCHEMA_EXPORT_FILENAME = "";
	public static final String DEFAULT_DB_FILE_NAME = "";
	public static final String DEFAULT_DB_FOLDER_PATH = System.getProperty("user.home");
	public static final MappingStrategy DEFAULT_MAPPING_STRATEGY = MappingStrategy.ONE_TABLE_PER_CLASS;
	
	public static final boolean DEFAULT_GENERATE_SCHEMA = true;
	public static final DbmsSupported DEFAULT_DBMS = DbmsSupported.GENERIC_SCHEMA;
	public static final boolean DEFAULT_ENUMARATION_TO_LOOKUP_TABLE = false;
	public static final boolean DEFAULT_STANTDARDIZE_NAMES = true;
	public static final boolean DEFAULT_GENERATE_INDEXES = false;
	
	public static final boolean DEFAULT_GENERATE_OBDA = false;
	public static final String DEFAULT_BASE_IRI = "https://example.com";
	public static final boolean DEFAULT_GENERATE_CONNECTION = false;
	
	public static final boolean DEFAULT_GENERATE_VIEWS = false;
	public static final boolean DEFAULT_GENERATE_INHERITED_ATTRIBURES = false;
	
	public static final boolean DEFAULT_GENERATE_TRIGGERS = false;
	public static final boolean DEFAULT_GENERATE_CLASS_DIAGRAM = false;
	public static final boolean DEFAULT_GENERATE_ER_DIAGRAM = false;
	

	@SerializedName("projectId")
	@Expose()
	private String id;

	@SerializedName("generateSchema")
	@Expose()
	private boolean generateSchema;
	
	@SerializedName("generateClassDiagram")
	@Expose()
	private boolean generateClassDiagram;
	
	@SerializedName("generateErDiagram")
	@Expose()
	private boolean generateErDiagram;
	
	@SerializedName("generateTrigger")
	@Expose()
	private boolean generateTrigger;

	@SerializedName("generateObda")
	@Expose()
	private boolean generateObda;
	
	@SerializedName("generateViews")
	@Expose()
	private boolean generateViews;

	@SerializedName("generateConnection")
	@Expose()
	private boolean generateConnection;
	
	@SerializedName("mappingStrategy")
	@Expose()
	private MappingStrategy mappingStrategy;

	@SerializedName("targetDBMS")
	@Expose()
	private DbmsSupported targetDBMS;

	@SerializedName("standarizeNames")
	@Expose()
	private boolean standardizeNames;

	@SerializedName("generateIndexes")
	@Expose()
	private boolean generateIndexes;

	@SerializedName("enumFieldToLookupTable")
	@Expose()
	private boolean enumFieldToLookupTable;

	@SerializedName("baseIri")
	@Expose()
	private String baseIri;

	@SerializedName("hostNameConnection")
	@Expose()
	private String hostNameConnection;

	@SerializedName("databaseNameConnection")
	@Expose()
	private String databaseNameConnection;

	@SerializedName("userNameConnection")
	@Expose()
	private String userNameConnection;

	@SerializedName("passwordConnectino")
	@Expose()
	private String passwordConnectino;

	@SerializedName("fileName")
	@Expose()
	private String fileName;

	@SerializedName("folderPath")
	@Expose()
	private String folderPath;
	
	@SerializedName("inheritedAttribures")
	@Expose()
	private boolean inheritedAttribures;

	/**
	 * Constructor without args to be called when deserializing project settings.
	 */
	public ProjectConfigurations() {
		this.id = "";
		this.setDefaultValues();
	}

	/**
	 * Initializes an instance of ProjectConfigurations with default settings.
	 *
	 * @param projectId - String containing the ID of the project related to
	 *                  initialized configuration.
	 */
	public ProjectConfigurations(String projectId) {
		this.id = projectId;
		this.setDefaultValues();
	}

	/**
	 * Resets default project configurations. By default, none of the options are
	 * enabled and the server's URL is the plugin's defaults.
	 */
	public void setDefaultValues() {
		this.generateSchema = ProjectConfigurations.DEFAULT_GENERATE_SCHEMA;
		this.generateObda = ProjectConfigurations.DEFAULT_GENERATE_OBDA;
		this.generateConnection = ProjectConfigurations.DEFAULT_GENERATE_CONNECTION;
		this.mappingStrategy = ProjectConfigurations.DEFAULT_MAPPING_STRATEGY;
		this.targetDBMS = ProjectConfigurations.DEFAULT_DBMS;
		this.standardizeNames = ProjectConfigurations.DEFAULT_STANTDARDIZE_NAMES;
		this.generateIndexes = ProjectConfigurations.DEFAULT_GENERATE_INDEXES;
		this.enumFieldToLookupTable = ProjectConfigurations.DEFAULT_ENUMARATION_TO_LOOKUP_TABLE;
		this.baseIri = ProjectConfigurations.DEFAULT_BASE_IRI;
		this.hostNameConnection = "";
		this.databaseNameConnection = "";
		this.userNameConnection = "";
		this.passwordConnectino = "";
		this.fileName = ProjectConfigurations.DEFAULT_DB_FILE_NAME;
		this.folderPath = ProjectConfigurations.DEFAULT_DB_FOLDER_PATH;
		this.generateViews = ProjectConfigurations.DEFAULT_GENERATE_VIEWS;
		this.inheritedAttribures = ProjectConfigurations.DEFAULT_GENERATE_INHERITED_ATTRIBURES;
		this.generateTrigger = ProjectConfigurations.DEFAULT_GENERATE_TRIGGERS;
		this.generateClassDiagram = ProjectConfigurations.DEFAULT_GENERATE_CLASS_DIAGRAM;
		this.generateErDiagram = ProjectConfigurations.DEFAULT_GENERATE_ER_DIAGRAM;
	}

	/**
	 * Returns the related project's ID.
	 *
	 * @return project's ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Informs the mapping strategy for the relational schema.
	 *
	 * @param mappingStrategy
	 */
	public void setMappingStrategy(MappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	/**
	 * Returns the mapping strategy for the relational schema.
	 *
	 * @return MappingStrategy
	 */
	public MappingStrategy getMappingStrategy() {
		return this.mappingStrategy;
	}

	/**
	 * Checks whether the nomenclature will be standardized in the database.
	 *
	 * @return boolean
	 */
	public boolean isStandardizeNames() {
		return standardizeNames;
	}

	/**
	 * Informs if the nomenclature will be standardized in the database.
	 *
	 * @param standarizeNames
	 */
	public void setStandardizeNames(boolean standarizeNames) {
		this.standardizeNames = standarizeNames;
	}

	/**
	 * Returns the target DBMS for generating the files.
	 *
	 * @return DBMSSuported
	 */
	public DbmsSupported getTargetDBMS() {
		return targetDBMS;
	}

	/**
	 * Informs the target DBMS for generating the files.
	 *
	 * @param targetDBMS
	 */
	public void setTargetDBMS(DbmsSupported targetDBMS) {
		this.targetDBMS = targetDBMS;
	}

	/**
	 * Returns if it is necessary to generate the relational schema for the
	 * ontology.
	 *
	 * @return boolean
	 */
	public boolean isGenerateSchema() {
		return this.generateSchema;
	}

	/**
	 * Informs if it is necessary to generate the relational schema for the
	 * ontology.
	 *
	 * @param generateSchema
	 */
	public void setGenerateSchema(boolean generateSchema) {
		this.generateSchema = generateSchema;
	}
	
	/**
	 * Returns if it is necessary to generate the triggers for
	 * constraints lost in the transformation process.
	 *
	 * @return boolean
	 */
	public boolean isGenerateTrigger() {
		return this.generateTrigger;
	}

	/**
	 * Informs if it is necessary to generate the relational schema for the
	 * ontology.
	 *
	 * @param generateSchema
	 */
	public void setGenerateTrigger(boolean generateTrigger) {
		this.generateTrigger = generateTrigger;
	}

	/**
	 * Returns if it is necessary to generate the OBDA file for the ontology.
	 *
	 * @return boolean
	 */
	public boolean isGenerateObda() {
		return this.generateObda;
	}

	/**
	 * Informs if it is necessary to generate the OBDA file for the ontology.
	 *
	 * @param generateSchema
	 */
	public void setGenerateObda(boolean generateObda) {
		this.generateObda = generateObda;
	}

	/**
	 * Returns if it is necessary to generate the connection for the database.
	 *
	 * @return boolean
	 */
	public boolean isGenerateConnection() {
		return generateConnection;
	}

	/**
	 * Informs if it is necessary to generate the connection for the database.
	 *
	 * @param generateConnection
	 */
	public void setGenerateConnection(boolean generateConnection) {
		this.generateConnection = generateConnection;
	}

	/**
	 * Returns the host name connection.
	 *
	 * @return string
	 */
	public String getHostNameConnection() {
		return hostNameConnection;
	}

	/**
	 * Informs the host name connection.
	 *
	 * @param hostName
	 */
	public void setHostNameConnection(String hostName) {
		this.hostNameConnection = hostName;
	}

	/**
	 * Returns the database name connection.
	 *
	 * @return string
	 */
	public String getDatabaseNameConnection() {
		return databaseNameConnection;
	}

	/**
	 * Informs the database name connection.
	 *
	 * @param databaseName
	 */
	public void setDatabaseNameConnection(String databaseName) {
		this.databaseNameConnection = databaseName;
	}

	/**
	 * Returns the user name connection.
	 *
	 * @return string
	 */
	public String getUserNameConnection() {
		return userNameConnection;
	}

	/**
	 * Informs the user name connection.
	 *
	 * @param userName
	 */
	public void setUserNameConnection(String userName) {
		this.userNameConnection = userName;
	}

	/**
	 * Returns the password connection.
	 *
	 * @return string
	 */
	public String getPasswordConnection() {
		return passwordConnectino;
	}

	/**
	 * Informs the password connection.
	 *
	 * @param password
	 */
	public void setPasswordConnection(String password) {
		this.passwordConnectino = password;
	}

	/**
	 * Returns the file name for mapping to ER.
	 *
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	};

	/**
	 * Informs the file name for mapping to ER.
	 *
	 * @param string
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the directory to save the files mapping.
	 *
	 * @return String
	 */
	public String getFolderPath() {
		return this.folderPath;
	}

	/**
	 * Informs the directory for mapping to ER.
	 *
	 * @param string
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * Returns if is necessary generate a table for the enumeration type fields.
	 *
	 * @return boolean
	 */
	public boolean isEnumFieldToLookupTable() {
		return this.enumFieldToLookupTable;
	}

	/**
	 * Informs if is necessary generate a table for the enumeration type fields.
	 *
	 * @param boolean
	 */
	public void setEnumFieldToLookupTable(boolean isEnumFieldToLookupTable) {
		this.enumFieldToLookupTable = isEnumFieldToLookupTable;
	}

	/**
	 * Returns if is necessary generate the index for the schema.
	 *
	 * @return boolean
	 */
	public boolean isGenerateIndex() {
		return this.generateIndexes;
	}

	/**
	 * Inform if the indexes will be generated.
	 *
	 * @param boolean
	 */
	public void setGenerateIndexes(boolean generateIndexes) {
		this.generateIndexes = generateIndexes;
	}
	
	/**
	 * Returns if is necessary generate the class diagram.
	 *
	 * @return boolean
	 */
	public void setGenerateClassDiagram(boolean generateClassDiagram) {
		this.generateClassDiagram = generateClassDiagram;
	}
	
	/**
	 * Inform if the class diagram will be generated.
	 *
	 * @param boolean
	 */
	public boolean isGenerateClassDiagram() {
		return this.generateClassDiagram;
	}

	/**
	 * Returns if is necessary generate the ER diagram.
	 *
	 * @return boolean
	 */
	public void setGenerateErDiagram(boolean generateErDiagram) {
		this.generateErDiagram = generateErDiagram;
	}
	
	/**
	 * Inform if the ER diagram will be generated.
	 *
	 * @param boolean
	 */
	public boolean isGenerateErDiagram() {
		return this.generateErDiagram;
	}
		
	/**
	 * Returns the IRI to generate OBDA file.
	 *
	 * @return string
	 */
	public String getBaseIri() {
		return this.baseIri;
	}

	/**
	 * Informs IRI to generate OBDA file.
	 *
	 * @param boolean
	 */
	public void setBaseIri(String iri) {
		this.baseIri = iri;
	}
	
	/**
	 * Returns if the project is configured to generate the views 
	 * @return
	 */
	public boolean isGenerateViews() {
		return this.generateViews;
	}
	
	/**
	 * Informs if the views will be generated.
	 * @param generateViews
	 */
	public void setGenerateViews(boolean generateViews) {
		this.generateViews = generateViews;
	}
	
	/**
	 * Returns if the views will be generated with inherited attributes.
	 * @return
	 */
	public boolean isInheritedAttributes() {
		return this.inheritedAttribures;
	}
	
	/**
	 * Informs if the views will be generated with inherited attributes.
	 * @param inheritedAttributes
	 */
	public void setInheritedAttributes(boolean inheritedAttributes) {
		this.inheritedAttribures = inheritedAttributes;
	}
}
