package pw.kitl.colorpicker.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xerca.xercapaint.client.ModClient;

import java.util.List;

@Mixin(ModClient.class)
public class ModClientMixin {
    @WrapOperation(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;asList([Ljava/lang/Object;)Ljava/util/List;"), require = 0)
    private <T> List<T> fixNull(T[] a, Operation<List<T>> original) {
        if (a != null) {
            return original.call((Object)a);
        } else {
            return List.of();
        }
    }
}
