package binnie.genetics.gui;

import binnie.core.craftgui.IWidget;
import binnie.core.craftgui.controls.ControlTextCentered;
import binnie.core.craftgui.geometry.Area;
import binnie.core.craftgui.minecraft.control.ControlItemDisplay;
import binnie.core.util.UniqueFluidStackSet;
import binnie.core.util.UniqueItemStackSet;
import binnie.extratrees.machines.brewery.BreweryRecipes;
import binnie.extratrees.machines.distillery.DistilleryRecipes;
import binnie.extratrees.machines.fruitpress.FruitPressRecipes;
import binnie.genetics.gui.bee.AnalystPageProducts;
import forestry.api.recipes.ICentrifugeRecipe;
import forestry.api.recipes.ISqueezerRecipe;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AnalystPageProduce extends ControlAnalystPage {
	public AnalystPageProduce(final IWidget parent, final Area area) {
		super(parent, area);
	}

	protected Collection<? extends ItemStack> getAllProducts(final ItemStack key) {
		final Collection<ItemStack> products = new UniqueItemStackSet();
		products.addAll(this.getCentrifuge(key));
		products.addAll(this.getSqueezer(key));
		products.add(FurnaceRecipes.instance().getSmeltingResult(key));
		products.addAll(this.getCrafting(key));
		return products;
	}

	public Collection<ItemStack> getCentrifuge(final ItemStack stack) {
		final List<ItemStack> products = new ArrayList<>();
		for (final ICentrifugeRecipe recipe : RecipeManagers.centrifugeManager.recipes()) {
			boolean isRecipe = false;
			if (stack.isItemEqual(recipe.getInput())) {
				isRecipe = true;
			}
			if (isRecipe) {
				for (final Object obj : recipe.getAllProducts().keySet()) {
					if (obj instanceof ItemStack) {
						products.add((ItemStack) obj);
					}
				}
			}
		}
		return products;
	}

	public NonNullList<ItemStack> getSqueezer(final ItemStack stack) {
		final NonNullList<ItemStack> products = NonNullList.create();
		for (ISqueezerRecipe recipe : RecipeManagers.squeezerManager.recipes()) {
			boolean isRecipe = false;
			for (final Object obj : recipe.getResources()) {
				if (obj instanceof ItemStack && stack.isItemEqual((ItemStack) obj)) {
					isRecipe = true;
				}
			}
			if (isRecipe) {
				if (!recipe.getRemnants().isEmpty()) {
					products.add(recipe.getRemnants());
				}
			}
		}
		return products;
	}

	public Collection<ItemStack> getCrafting(final ItemStack stack) {
		final List<ItemStack> products = new ArrayList<>();
		for (final Object recipeO : CraftingManager.getInstance().getRecipeList()) {
			if (recipeO instanceof ShapelessRecipes) {
				final ShapelessRecipes recipe = (ShapelessRecipes) recipeO;
				boolean match = true;
				for (final Object rec : recipe.recipeItems) {
					if (rec != null && (!(rec instanceof ItemStack) || !stack.isItemEqual((ItemStack) rec))) {
						match = false;
					}
				}
				if (match) {
					products.add(recipe.getRecipeOutput());
				}
			}
			if (recipeO instanceof ShapedRecipes) {
				final ShapedRecipes recipe2 = (ShapedRecipes) recipeO;
				boolean match = true;
				for (final Object rec2 : recipe2.recipeItems) {
					if (rec2 != null && (!(rec2 instanceof ItemStack) || !stack.isItemEqual((ItemStack) rec2))) {
						match = false;
					}
				}
				if (match) {
					products.add(recipe2.getRecipeOutput());
				}
			}
			if (recipeO instanceof ShapelessOreRecipe) {
				final ShapelessOreRecipe recipe3 = (ShapelessOreRecipe) recipeO;
				boolean match = true;
				for (final Object rec : recipe3.getInput()) {
					if (rec != null && (!(rec instanceof ItemStack) || !stack.isItemEqual((ItemStack) rec))) {
						match = false;
					}
				}
				if (!match) {
					continue;
				}
				products.add(recipe3.getRecipeOutput());
			}
		}
		return products;
	}

	public Collection<FluidStack> getAllFluids(final ItemStack stack) {
		final List<FluidStack> products = new ArrayList<>();
		products.addAll(this.getSqueezerFluid(stack));
		if (FruitPressRecipes.getOutput(stack) != null) {
			products.add(FruitPressRecipes.getOutput(stack));
		}
		return products;
	}

	public Collection<FluidStack> getSqueezerFluid(final ItemStack stack) {
		final List<FluidStack> products = new ArrayList<>();
		for (final ISqueezerRecipe recipe : RecipeManagers.squeezerManager.recipes()) {
			boolean isRecipe = false;
			for (final Object obj : recipe.getResources()) {
				if (obj instanceof ItemStack && stack.isItemEqual((ItemStack) obj)) {
					isRecipe = true;
				}
			}
			if (isRecipe) {
				products.add(recipe.getFluidOutput());
			}
		}
		return products;
	}

	protected Collection<? extends FluidStack> getAllProducts(final FluidStack stack) {
		final Collection<FluidStack> fluids = new UniqueFluidStackSet();
		fluids.add(BreweryRecipes.getOutput(stack));
		fluids.add(DistilleryRecipes.getOutput(stack, 0));
		fluids.add(DistilleryRecipes.getOutput(stack, 1));
		fluids.add(DistilleryRecipes.getOutput(stack, 2));
		return fluids;
	}

	protected Collection<ItemStack> getAllProductsAndFluids(final Collection<ItemStack> collection) {
		final Collection<ItemStack> products = new UniqueItemStackSet();
		for (final ItemStack stack : collection) {
			products.addAll(this.getAllProducts(stack));
		}
		final Collection<ItemStack> products2 = new UniqueItemStackSet();
		for (final ItemStack stack2 : products) {
			products2.addAll(this.getAllProducts(stack2));
		}
		final Collection<ItemStack> products3 = new UniqueItemStackSet();
		for (final ItemStack stack3 : products2) {
			products3.addAll(this.getAllProducts(stack3));
		}
		products.addAll(products2);
		products.addAll(products3);
		final Collection<FluidStack> allFluids = new UniqueFluidStackSet();
		for (final ItemStack stack4 : collection) {
			allFluids.addAll(this.getAllFluids(stack4));
		}
		final Collection<FluidStack> fluids2 = new UniqueFluidStackSet();
		for (final FluidStack stack5 : allFluids) {
			fluids2.addAll(this.getAllProducts(stack5));
		}
		final Collection<FluidStack> fluids3 = new UniqueFluidStackSet();
		for (final FluidStack stack6 : fluids2) {
			fluids3.addAll(this.getAllProducts(stack6));
		}
		allFluids.addAll(fluids2);
		allFluids.addAll(fluids3);
		for (final FluidStack fluid : allFluids) {
			ItemStack container = AnalystPageProducts.getContainer(fluid);
			if (container != null) {
				products.add(container);
			}
		}
		return products;
	}

	protected int getRefined(final String string, int y, final Collection<ItemStack> products) {
		new ControlTextCentered(this, y, string).setColour(this.getColour());
		y += 10;
		final int maxBiomePerLine = (this.width() + 2 - 16) / 18;
		final int biomeListX = (this.width() - (Math.min(maxBiomePerLine, products.size()) * 18 - 2)) / 2;
		int dx = 0;
		int dy = 0;
		for (final ItemStack soilStack : products) {
			if (dx >= 18 * maxBiomePerLine) {
				dx = 0;
				dy += 18;
			}
			final FluidStack fluid = FluidUtil.getFluidContained(soilStack);
			soilStack.setCount(1);
			final ControlItemDisplay display = new ControlItemDisplay(this, biomeListX + dx, y + dy, soilStack, fluid == null);
			if (fluid != null) {
				display.addTooltip(fluid.getLocalizedName());
			}
			dx += 18;
		}
		if (dx != 0) {
			dy += 18;
		}
		y += dy;
		return y;
	}
}
