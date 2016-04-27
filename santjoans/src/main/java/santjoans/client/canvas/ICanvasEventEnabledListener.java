package santjoans.client.canvas;

public interface ICanvasEventEnabledListener {
	public void fireEvent(int x, int y, int eventType);
	public void fireTouchEvent(int x, int y, int eventType);
}
