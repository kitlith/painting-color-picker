package pw.kitl.colorpicker.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
//? if >= 1.21.11 {
/*import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
*///? }
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import xerca.xercapaint.PaletteUtil;
import xerca.xercapaint.client.BasePalette;

//? if >= 1.21.11
//import java.util.List;

@Mixin(BasePalette.class)
public abstract class BasePaletteMixin extends Screen {
    protected BasePaletteMixin(Component component) {
        super(component);
    }

    @Shadow
    @Final
    private static float CUSTOM_COLOR_RADIUS;

    @Shadow
    @Final
    private static Vec2[] CUSTOM_COLOR_CENTERS;

    @Shadow
    double paletteX;
    @Shadow
    double paletteY;
    @Shadow
    boolean isPickingColor;

    @Shadow
    @Final
    PaletteUtil.CustomColor[] customColors;

    @Shadow
    abstract float sqrDist(Vec2 a, Vec2 b);

    @Unique
    protected void renderColorTooltip(GuiGraphics guiGraphics, int color, int mouseX, int mouseY) {
        Component component = Component.literal("#%06x".formatted(color & 0xFFFFFF));
        //? if >= 1.21.11 {
        /*guiGraphics.renderTooltip(
                font,
                List.of(ClientTooltipComponent.create(component.getVisualOrderText())),
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                null
        );
        *///? } else {
        guiGraphics.renderTooltip(font, component, mouseX, mouseY);
        //? }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderPicker(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        float sqrCustomRadius = CUSTOM_COLOR_RADIUS * CUSTOM_COLOR_RADIUS;
        Vec2 hoverVec = new Vec2(mouseX, mouseY);

        for (int i = 0; i < customColors.length; i++) {
            int x = (int) paletteX + (int) CUSTOM_COLOR_CENTERS[i].x;
            int y = (int) paletteY + (int) CUSTOM_COLOR_CENTERS[i].y;
            Vec2 customColorVec = new Vec2(x, y);
            if (this.customColors[i].numberOfColors > 0 && this.sqrDist(hoverVec, customColorVec) <= sqrCustomRadius) {
                int customColor = customColors[i].getColor().rgbVal();
                this.renderColorTooltip(guiGraphics, customColor, mouseX, mouseY);
            }
        }
    }
}