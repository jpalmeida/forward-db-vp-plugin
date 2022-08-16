package br.ufes.inf.nemo.ontoumltodb.vp.settings;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.vp.plugin.ApplicationManager;
import com.vp.plugin.model.IProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Configurations {

	private static final String CONFIG_FILE_NAME = "config_ontoumltodb.json";

	private static Configurations instance;

	@Expose()
	private List<ProjectConfigurations> projects;

	private Configurations() {
		this.projects = new ArrayList<ProjectConfigurations>();
	}

	private List<ProjectConfigurations> getProjectConfigurationsList() {
		return projects;
	}

	private boolean addProjectConfigurations(ProjectConfigurations projectConfigurations) {
		return getProjectConfigurationsList().add(projectConfigurations);
	}

	/**
	 * Returns singleton instance of the class.
	 *
	 * @return configurations
	 */
	public static Configurations getInstance() {
		if (instance == null) {
			final ApplicationManager application = ApplicationManager.instance();
			final File workspace = application.getWorkspaceLocation();
			final File configurationsFile = new File(workspace, CONFIG_FILE_NAME);

			if (configurationsFile.exists()) {
				String json = "";
				try {
					json = new String(Files.readAllBytes(configurationsFile.toPath()));
					Gson gson = new Gson();
					instance = gson.fromJson(json, Configurations.class);
				} catch (Exception e) {
					if (e instanceof IOException)
						application.getViewManager().showMessage("Unable to load configuration file.\n");
					else if (e instanceof JsonSyntaxException)
						application.getViewManager().showMessage("Configuration file ill-formed.\n");
					else
						application.getViewManager().showMessage("Unknown error while reading configuration file.\n");

					e.printStackTrace();
				}
			}

			if (instance == null)
				instance = new Configurations();
		}

		return instance;
	}

	/** Persists user preferences on Visual Paradigm's workspace. */
	public void save() {
		final ApplicationManager application = ApplicationManager.instance();
		final File workspace = application.getWorkspaceLocation();
		final File configurationsFile = new File(workspace, CONFIG_FILE_NAME);
		final FileWriter fw;
		final GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT,
				context) -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()));

		builder.setPrettyPrinting();

		final Gson gson = builder.create();
		final String json = gson.toJson(this);

		try {
			if (!configurationsFile.exists()) {
				configurationsFile.createNewFile();
			}

			fw = new FileWriter(configurationsFile);
			fw.write(json);
			fw.close();
		} catch (IOException e) {
			application.getViewManager().showMessage("Unable save the configurations.");
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the configurations of the current project.
	 *
	 * @return current project's configurations.
	 */
	public ProjectConfigurations getProjectConfigurations() {
		final IProject current = ApplicationManager.instance().getProjectManager().getProject();

		return getProjectConfigurations(current.getId());
	}

	/**
	 * Retrieves the project configurations given the provided ID. If no previous
	 * configurations is present, a new instance of
	 * <code>ProjectConfigurations</code> is returned with default settings.
	 *
	 * @param projectId - Interest project's ID.
	 * @return current project's configurations.
	 */
	public ProjectConfigurations getProjectConfigurations(String projectId) {
		for (ProjectConfigurations projectConfigurations : getProjectConfigurationsList()) {
			if (projectConfigurations.getId().equals(projectId)) {
				return projectConfigurations;
			}
		}

		ProjectConfigurations projectConfigurations = new ProjectConfigurations(projectId);
		addProjectConfigurations(projectConfigurations);

		return projectConfigurations;
	}
}
