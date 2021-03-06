package binnie.core.craftgui.events;

import binnie.core.craftgui.IWidget;

public class EventToggleButtonClicked extends Event {
	boolean toggled;

	public EventToggleButtonClicked(final IWidget origin, final boolean toggled) {
		super(origin);
		this.toggled = toggled;
	}

	public boolean isActive() {
		return this.toggled;
	}
}
