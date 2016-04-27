package santjoans.client.piezes.navigator.preview;

import santjoans.client.piezes.navigator.viewer.IControllerViewerCommands;

public class PreviewWidgetControlImpl extends PreviewWidgetAbstractControl {
	
	private boolean sync; 

	public PreviewWidgetControlImpl(IControllerViewerCommands piezeViewerCommands, boolean syncViewer) {
		super(piezeViewerCommands, syncViewer);
	}

	protected void moveStep(final PreviewWidgetContext context) {
		if (sync) {
			piezeViewerCommands.setPosition(context.getStartX(), context.getStartY());
		} else {
			updatePreview(context);
		}
	}
	
	protected void moveFinish(final PreviewWidgetContext context) {
		if (sync) {
			piezeViewerCommands.setPosition(context.getStartX(), context.getStartY());
		} else {
			piezeViewerCommands.setPosition(context.getStartX(), context.getStartY());
		}
	}

	@Override
	protected void autoSync(boolean sync) {
		this.sync = sync;
	}
}
