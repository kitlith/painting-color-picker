package pw.kitl.colorpicker.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xerca.xercapaint.client.GuiCanvasEdit;

@Mixin(GuiCanvasEdit.class)
public abstract class GuiCanvasEditMixin extends BasePaletteMixin {
    protected GuiCanvasEditMixin(Component component) {
        super(component);
    }

    @Shadow private boolean gettingSigned;
    @Shadow private double canvasX;
    @Shadow private double canvasY;
    @Shadow @Final private int canvasPixelScale;
    @Shadow @Final private int canvasPixelWidth;
    @Shadow @Final private int canvasPixelHeight;

    @Shadow
    private boolean inCanvas(int x, int y) {
        return false;
    }

    @Shadow
    private int getPixelAt(int x, int y) { return 0; }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderPicker(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float f, CallbackInfo info) {
        if (!this.gettingSigned && inCanvas(mouseX, mouseY) && isPickingColor) {
            int x = (mouseX - (int) canvasX) / canvasPixelScale;
            int y = (mouseY - (int) canvasY) / canvasPixelScale;
            if (x >= 0 && y >= 0 && x < canvasPixelWidth && y < canvasPixelHeight) {
                int color = getPixelAt(x, y);
                this.renderColorTooltip(guiGraphics, color, mouseX, mouseY);
            }
        }
    }
}
