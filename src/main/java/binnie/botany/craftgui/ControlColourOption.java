package binnie.botany.craftgui;

import binnie.botany.api.IFlowerColour;
import binnie.core.craftgui.Attribute;
import binnie.core.craftgui.CraftGUI;
import binnie.core.craftgui.controls.listbox.ControlList;
import binnie.core.craftgui.controls.listbox.ControlTextOption;
import binnie.core.craftgui.geometry.CraftGUIUtil;
import binnie.core.craftgui.geometry.Point;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ControlColourOption extends ControlTextOption<IFlowerColour> {
	ControlColourDisplay controlBee;
	Point boxPosition;

	public ControlColourOption(final ControlList<IFlowerColour> controlList, final IFlowerColour option, final int y) {
		super(controlList, option, option.getColourName(), y);
		this.setSize(new Point(this.getSize().x(), 20));
		this.controlBee = new ControlColourDisplay(this, 2, 2, option);
		this.addAttribute(Attribute.MouseOver);
		CraftGUIUtil.moveWidget(this.textWidget, new Point(22, 0));
		this.textWidget.setSize(this.textWidget.getSize().sub(new Point(24, 0)));
		final int th = CraftGUI.render.textHeight(this.textWidget.getValue(), this.textWidget.getSize().x());
		final int height = Math.max(20, th + 6);
		this.setSize(new Point(this.size().x(), height));
		this.textWidget.setSize(new Point(this.textWidget.getSize().x(), height));
		this.boxPosition = new Point(2, (height - 18) / 2);
	}
}
