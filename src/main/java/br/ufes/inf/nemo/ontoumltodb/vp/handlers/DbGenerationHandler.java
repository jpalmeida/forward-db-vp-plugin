package br.ufes.inf.nemo.ontoumltodb.vp.handlers;

import java.awt.Component;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.ViewManager;
import com.vp.plugin.view.IDialog;
import com.vp.plugin.view.IDialogHandler;

import br.ufes.inf.nemo.ontoumltodb.vp.settings.Configurations;
import br.ufes.inf.nemo.ontoumltodb.vp.settings.ProjectConfigurations;
import br.ufes.inf.nemo.ontoumltodb.vp.views.DbGenerationView;

public class DbGenerationHandler implements IDialogHandler {

	private IDialog dialog;
	private final DbGenerationView view;
	private final ViewManager viewManager;
	private boolean wasShown = false;
	private boolean wasClosed = false;
	private boolean wasCancelled = false;

	public DbGenerationHandler(ProjectConfigurations projectConfigurations) {
		view = new DbGenerationView(projectConfigurations);
		viewManager = ApplicationManager.instance().getViewManager();

		view.onExport(e -> {
			if (view.checkValidParamters()) {
				view.updateConfigurationsValues(projectConfigurations);
				Configurations.getInstance().save();
				closeDialog();
			}
		});
		view.onCancel(e -> {
			wasCancelled = true;
			closeDialog();
		});
	}

	@Override
	public boolean canClosed() {
		wasCancelled = true;
		wasClosed = true;
		return true;
	}

	@Override
	public Component getComponent() {
		return view;
	}

	@Override
	public void prepare(IDialog dialog) {
		this.dialog = dialog;
		dialog.setTitle("Generates the relational schema and its mappings.");
		dialog.setModal(true);
		dialog.setResizable(false); // true
		dialog.setSize(view.getWidth(), view.getHeight() + 20);
		dialog.pack();
	}

	@Override
	public void shown() {
	}

	private void closeDialog() {
		if (wasClosed) {
			System.out.println("Mapping to ER dialog was already closed.");
		} else if (!wasShown) {
			System.out.println("Mapping to ER dialog was never shown. Setting wasClosed to \"true\"");
		} else {
			System.out.println("Closing mapping do ER dialog.");
			dialog.close();
		}
		wasClosed = true;
	}

	public void showDialog() {
		if (!wasClosed) {
			wasShown = true;
			viewManager.showDialog(this);
		}
	}

	public boolean wasCancelled() {
		return wasCancelled;
	}
}
