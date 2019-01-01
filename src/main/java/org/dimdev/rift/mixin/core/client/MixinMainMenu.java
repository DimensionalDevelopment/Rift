package org.dimdev.rift.mixin.core.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.dimdev.rift.modlist.ModList;
import org.dimdev.riftloader.RiftLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinMainMenu extends GuiScreen {

    @Shadow
    private void switchToRealms() {
    }

    @Shadow @Final private RenderSkybox panorama;
    @Shadow @Final private float minceraftRoll;
    @Shadow @Final private static ResourceLocation MINECRAFT_TITLE_TEXTURES;
    @Shadow @Final private static ResourceLocation MINECRAFT_TITLE_EDITION;
    @Shadow private String splashText;
    @Shadow private int widthCopyrightRest;
    @Shadow private int widthCopyright;
    @Shadow private String openGLWarning1;
    @Shadow private int openGLWarningX1;
    @Shadow private int openGLWarningY1;
    @Shadow private int openGLWarningX2;
    @Shadow private int openGLWarningY2;
    @Shadow private String openGLWarning2;
    @Shadow private int openGLWarning2Width;
    @Shadow private GuiScreen realmsNotification;

    @Shadow protected abstract boolean areRealmsNotificationsEnabled();

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.panorama.render(p_render_3_);
        int lvt_5_1_ = this.width / 2 - 137;
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/title/background/panorama_overlay.png"));
        drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, 16, 128, this.width, this.height, 16.0F, 128.0F);
        this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if ((double) minceraftRoll < 1.0E-4D) {
            this.drawTexturedModalRect(lvt_5_1_, 30, 0, 0, 99, 44);
            this.drawTexturedModalRect(lvt_5_1_ + 99, 30, 129, 0, 27, 44);
            this.drawTexturedModalRect(lvt_5_1_ + 99 + 26, 30, 126, 0, 3, 44);
            this.drawTexturedModalRect(lvt_5_1_ + 99 + 26 + 3, 30, 99, 0, 26, 44);
            this.drawTexturedModalRect(lvt_5_1_ + 155, 30, 0, 45, 155, 44);
        } else {
            this.drawTexturedModalRect(lvt_5_1_, 30, 0, 0, 155, 44);
            this.drawTexturedModalRect(lvt_5_1_ + 155, 30, 0, 45, 155, 44);
        }

        this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
        drawModalRectWithCustomSizedTexture(lvt_5_1_ + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float lvt_7_1_ = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.milliTime() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
        lvt_7_1_ = lvt_7_1_ * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
        GlStateManager.scalef(lvt_7_1_, lvt_7_1_, lvt_7_1_);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String lvt_8_1_ = "Minecraft 1.13.2";
        if (this.mc.isDemo()) {
            lvt_8_1_ = lvt_8_1_ + " Demo";
        } else {
            lvt_8_1_ = lvt_8_1_ + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType());
        }

        this.drawString(this.fontRenderer, lvt_8_1_, 2, this.height - 30, -1);
        this.drawString(this.fontRenderer, "Powered By Rift @VERSION@", 2, this.height - 20, -1);
        this.drawString(this.fontRenderer, String.format("%s mods loaded", RiftLoader.instance.getMods().size()), 2, this.height - 10, -1);


        this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, -1);
        if (p_render_1_ > this.widthCopyrightRest && p_render_1_ < this.widthCopyrightRest + this.widthCopyright && p_render_2_ > this.height - 10 && p_render_2_ < this.height) {
            drawRect(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, -1);
        }

        if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
            drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
            this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
            this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, this.openGLWarningY1 + 12, -1);
        }

        super.render(p_render_1_, p_render_2_, p_render_3_);
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.render(p_render_1_, p_render_2_, p_render_3_);
        }
    }


    @ModifyArg(
            method = "addSingleplayerMultiplayerButtons",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;addButton(Lnet/minecraft/client/gui/GuiButton;)Lnet/minecraft/client/gui/GuiButton;",
                    ordinal = 2
            )
    )
    private GuiButton getRealmsButton(GuiButton original) {
        GuiButton button = new GuiButton(original.id, width / 2 + 2, original.y, 98, 20, I18n.format("menu.online")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                switchToRealms();
            }
        };
        return button;
    }
    
    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At("RETURN"))
    private void onAddSingleplayerMultiplayerButtons(int y, int dy, CallbackInfo ci) {
        GuiButton button = new GuiButton(100, width / 2 - 100, y + dy * 2, 98, 20, I18n.format("rift.modlist.mods")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                ModList.displayModList();
            }
        };
        addButton(button);
    }
    
}
