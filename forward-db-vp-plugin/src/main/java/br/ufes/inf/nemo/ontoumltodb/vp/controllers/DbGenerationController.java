package br.ufes.inf.nemo.ontoumltodb.vp.controllers;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;

import br.ufes.inf.nemo.ontoumltodb.vp.LoadGraph;
import br.ufes.inf.nemo.ontoumltodb.transformation.OntoUmlToDb;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.vp.handlers.DbGenerationHandler;
import br.ufes.inf.nemo.ontoumltodb.vp.settings.Configurations;
import br.ufes.inf.nemo.ontoumltodb.vp.settings.ProjectConfigurations;

public class DbGenerationController implements VPActionController {

	private Configurations configurations;
	private ProjectConfigurations projectConfigurations;
	private String fileDirectory;
	private String schemaFormat = ".sql";
	private String obdaFormat = ".obda";
	private String propertiesFormat = ".properties";
	private String viewsFormat = ".views.sql";
	private Path schemaFilePath;
	private Path obdaFilePath;
	private Path propertiesFilePath;
	private Path viewsFilePath;

	@Override
	public void performAction(VPAction arg0) {
		configurations = Configurations.getInstance();
		projectConfigurations = configurations.getProjectConfigurations();

		DbGenerationHandler dbTransformationHandler = new DbGenerationHandler(projectConfigurations);
		dbTransformationHandler.showDialog();

		if (dbTransformationHandler.wasCancelled()) {
			return;
		}
		
		OntoUmlToDb toDb = new OntoUmlToDb(LoadGraph.load());

		toDb.setMappingStrategy(projectConfigurations.getMappingStrategy());
		
		toDb.setDbms(projectConfigurations.getTargetDBMS());
		toDb.setEnumFieldToLookupTable(projectConfigurations.isEnumFieldToLookupTable());
		toDb.setStandardizeNames(projectConfigurations.isStandardizeNames());
		toDb.setBaseIri(projectConfigurations.getBaseIri());
		toDb.setHostNameConnection(projectConfigurations.getHostNameConnection());
		toDb.setDatabaseNameConnection(projectConfigurations.getDatabaseNameConnection());
		toDb.setUserNameConnection(projectConfigurations.getUserNameConnection());
		toDb.setPasswordConnection(projectConfigurations.getPasswordConnection());
		
		toDb.setPutInheritedAttributes(projectConfigurations.isInheritedAttributes());

		toDb.runTransformation();

		if (	projectConfigurations.isGenerateSchema() || projectConfigurations.isGenerateObda() ||
				projectConfigurations.isGenerateConnection() || projectConfigurations.isGenerateViews() ||
				projectConfigurations.isGenerateTrigger()
			){ 
			if(putFilePath()) {
				saveFiles(toDb);
			}
		}
		
		if(projectConfigurations.isGenerateClassDiagram()) {
			ClassDiagramBuilder.builder(toDb.getIntermediateGraph()).run();
		}
		
		if(projectConfigurations.isGenerateErDiagram()) {
			ErDiagramBuilder.builder(toDb.getGraph() ).run();
		}
	}

	/**
	 * Called when the menu containing the button is accessed allowing for action
	 * manipulation, such as enable/disable or selecting the button.
	 *
	 * <p>
	 * OBS: DOES NOT apply to this class.
	 */
	@Override
	public void update(VPAction arg0) {
	}

	private boolean putFilePath() {
		FileDialog fileDialog;
		Frame rootFrame = (Frame) ApplicationManager.instance().getViewManager().getRootFrame();
		
		String suggestedFolderPath = projectConfigurations.getFolderPath();
		String suggestedFileName = projectConfigurations.getFileName();

		if (suggestedFileName.isEmpty()) {
			suggestedFileName = ApplicationManager.instance().getProjectManager().getProject().getName();
		}

		final String title = "Choose file destination";
		fileDialog = new FileDialog(rootFrame, title, FileDialog.SAVE);

		fileDialog.setFile(suggestedFileName);
		fileDialog.setDirectory(suggestedFolderPath);
		fileDialog.setMultipleMode(false);
		fileDialog.setVisible(true);

		fileDirectory = fileDialog.getDirectory();
		final String fileName = removeFileExtension(fileDialog.getFile());

		if (fileDirectory != null && fileName != null) {
			schemaFilePath = Paths.get(fileDirectory, fileName + schemaFormat);
			obdaFilePath = Paths.get(fileDirectory, fileName + obdaFormat);
			propertiesFilePath = Paths.get(fileDirectory, fileName + propertiesFormat);
			viewsFilePath = Paths.get(fileDirectory, fileName + viewsFormat);
			return true;
		} else {
			return false;
		}
	}
	
	private void saveFiles(OntoUmlToDb toDb) {
		String script = "";
		try {

			saveFilePath();

			if (projectConfigurations.isGenerateSchema()) {
				script = toDb.getRelationalSchemaScript();
				if(projectConfigurations.isGenerateIndex()) {
					script += "\n\n";
					script += toDb.getIndexesScript();
				}
				Files.write(schemaFilePath, script.getBytes());
			}
			
			if (projectConfigurations.isGenerateObda())
				Files.write(obdaFilePath, toDb.getObdaScript().getBytes());
			
			if (projectConfigurations.isGenerateConnection()) 
				Files.write(propertiesFilePath, toDb.getConnectionScript().getBytes());
			
			if(projectConfigurations.isGenerateViews())
				Files.write(viewsFilePath, toDb.getViewsScript().getBytes());
			
			if(projectConfigurations.isGenerateTrigger()) 
				saveTriggers(toDb.getTriggersScripts());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveTriggers(ArrayList<TriggerResult> triggerResults) throws IOException {
		Path schemaTriggerPath;
		String fileName;
		
		String allTriggers = "";
		
		for(TriggerResult triggerResult : triggerResults) {
			
			allTriggers += triggerResult.getScript() + "\n\n";
			
			fileName = triggerResult.getName();
			
			schemaTriggerPath = Paths.get(fileDirectory, fileName + schemaFormat);
			
			Files.write(schemaTriggerPath, triggerResult.getScript().getBytes());
		}
		
		schemaTriggerPath = Paths.get(fileDirectory, "AllTriggers" + schemaFormat);
		
		Files.write(schemaTriggerPath, allTriggers.getBytes());
	}
	
	private void saveFilePath() {
		final Path directoryPath = schemaFilePath.getParent();
		final String directoryPathName = directoryPath.toAbsolutePath().getFileName().toString();
		final String filePathName = removeFileExtension(schemaFilePath.getFileName().toString());

		projectConfigurations.setFolderPath(directoryPathName);
		projectConfigurations.setFileName(filePathName);
		configurations.save();
	}
	
	private String removeFileExtension(String fileName) {
		  if(fileName.lastIndexOf('.') > 0)
			  return fileName.substring(0, fileName.lastIndexOf('.'));
		  else return fileName;
	  }
}
