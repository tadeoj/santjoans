package santjoans.client.imagebutton;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;

public class ImageButton extends Image {
	
	enum State {
		PRESSED,
		UNPRESSED,
		DISABLED
	}

	private State state = State.DISABLED;
	
	private String cursor;
	private IButtonPressed buttonPressedListener;
	
	final static String UNPRESSED_MARGIN = "2px 2px 2px 2px";
	final static String PRESSED_MARGIN = "4px 1px 0px 3px";
	final static String DISABLED_FILTER = "alpha(opacity=40)";
	final static String ENABLED_FILTER = "alpha(opacity=100)";
	final static String DISABLED_OPACITY = "0.4";
	final static String ENABLED_OPACITY = "1";

	
	public ImageButton(ImageResource imageResource) {
		super(imageResource);
		state = State.UNPRESSED;
		updateState();
		init();
	}

	protected void init() {
		addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				if (state != State.DISABLED) {
					state = State.PRESSED;
					updateState();
					if (buttonPressedListener != null) {
						buttonPressedListener.buttonPressed(ImageButton.this);
					}
				}
			}
		});
		addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (state != State.DISABLED) {
					state = State.UNPRESSED;
					updateState();
				}
			}
		});
		addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				cursor = DOM.getStyleAttribute(getElement(), "cursor");
				DOM.setStyleAttribute(getElement(), "cursor", "pointer");
			}
		});
		addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				DOM.setStyleAttribute(getElement(), "cursor", cursor);
				if (state == State.PRESSED)
					state = State.UNPRESSED;
			}
		});
	}

	public void updateState() {
		DOM.setStyleAttribute(getElement(), "margin", state == State.PRESSED ? PRESSED_MARGIN : UNPRESSED_MARGIN);
		DOM.setStyleAttribute(getElement(), "opacity", state == State.DISABLED ? DISABLED_OPACITY : ENABLED_OPACITY);
		DOM.setStyleAttribute(getElement(), "filter", state == State.DISABLED ? DISABLED_FILTER : ENABLED_FILTER);
	}
	
	public void setButtonPressedListener(IButtonPressed buttonPressed) {
		this.buttonPressedListener = buttonPressed;
	}
	
	public void setEnabled(boolean enabled) {
		if (enabled) {
			if (state == State.DISABLED) {
				state = State.UNPRESSED;
				updateState();
			}
		} else {
			if (state != State.DISABLED) {
				state = State.DISABLED;
				updateState();
			}
		}
	}
	
	public boolean isEnabled() {
		return state != State.DISABLED;
	}

}
