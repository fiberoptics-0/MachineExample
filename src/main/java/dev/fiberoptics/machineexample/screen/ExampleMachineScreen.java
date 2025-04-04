package dev.fiberoptics.machineexample.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.fiberoptics.machineexample.MachineExample;
import dev.fiberoptics.machineexample.block.entity.ExampleMachineBlockEntity;
import dev.fiberoptics.machineexample.menu.ExampleMachineMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;

import java.awt.*;

public class ExampleMachineScreen extends AbstractContainerScreen<ExampleMachineMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MachineExample.MODID, "textures/gui/example_machine.png");

    public ExampleMachineScreen(ExampleMachineMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        guiGraphics.blit(TEXTURE,0,0,0,0,imageWidth,imageHeight);
        int scaledProgress = (int)(22 * (((float)menu.data.get(0))/((float)menu.data.get(1))));
        guiGraphics.blit(TEXTURE,80,35,176,0,scaledProgress,16);
        int scaledEnergy = (int)(73 * (((float)menu.data.get(2))/((float)menu.data.get(3))));
        guiGraphics.blit(TEXTURE,55,24,176,16,scaledEnergy,3);
        drawFluid(guiGraphics);
        pose.popPose();
    }

    private void drawFluid(GuiGraphics guiGraphics) {
        if(menu.blockEntity.getFluidStack(0).getAmount() > 0) {
            FluidStack fluid = menu.blockEntity.getFluidStack(0);
            IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluid.getFluid());
            Color color = new Color(properties.getTintColor(fluid),true);
            ResourceLocation location = properties.getStillTexture(fluid);
            TextureAtlasSprite texture =
                    Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture.atlasLocation());
            RenderSystem.setShaderColor(color.getRed()/255F, color.getGreen()/255F,
                    color.getBlue()/255F, color.getAlpha()/255F);
            BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = guiGraphics.pose().last().pose();
            int scaledHeight = (int)(41*(((float)fluid.getAmount()) /((float)menu.blockEntity.getCapacity(0))));
            for(int i = 19; i <= 46; i+=16) {
                int width = Math.min(46-i+1,16);
                for(int j = 65-scaledHeight; j <= 64; j+=16) {
                    int height = Math.min(64-j+1,16);
                    float minU = texture.getU0();
                    float minV = texture.getV0();
                    float maxU = texture.getU(width);
                    float maxV = texture.getV(height);
                    vertexBuffer.vertex(matrix4f,i,j+height,0).uv(minU,maxV).endVertex();
                    vertexBuffer.vertex(matrix4f,i+width,j+height,0).uv(maxU,maxV).endVertex();
                    vertexBuffer.vertex(matrix4f,i+width,j,0).uv(maxU,minV).endVertex();
                    vertexBuffer.vertex(matrix4f,i,j,0).uv(minU,minV).endVertex();
                }
            }
            BufferUploader.drawWithShader(vertexBuffer.end());
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if(menu.blockEntity.getFluidStack(1).getAmount() > 0) {
            FluidStack fluid = menu.blockEntity.getFluidStack(1);
            IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluid.getFluid());
            Color color = new Color(properties.getTintColor(fluid),true);
            ResourceLocation location = properties.getStillTexture(fluid);
            TextureAtlasSprite texture =
                    Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, texture.atlasLocation());
            RenderSystem.setShaderColor(color.getRed()/255F, color.getGreen()/255F,
                    color.getBlue()/255F, color.getAlpha()/255F);
            BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
            RenderSystem.enableBlend();
            vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = guiGraphics.pose().last().pose();
            int scaledHeight = (int)(41*(((float)fluid.getAmount()) /((float)menu.blockEntity.getCapacity(1))));
            for(int i = 136; i <= 163; i += 16) {
                int width = Math.min(163-i+1,16);
                for(int j = 65-scaledHeight; j <= 64; j+=16) {
                    int height = Math.min(64-j+1,16);
                    float minU = texture.getU0();
                    float minV = texture.getV0();
                    float maxU = texture.getU(width);
                    float maxV = texture.getV(height);
                    vertexBuffer.vertex(matrix4f,i,j+height,0).uv(minU,maxV).endVertex();
                    vertexBuffer.vertex(matrix4f,i+width,j+height,0).uv(maxU,maxV).endVertex();
                    vertexBuffer.vertex(matrix4f,i+width,j,0).uv(maxU,minV).endVertex();
                    vertexBuffer.vertex(matrix4f,i,j,0).uv(minU,minV).endVertex();
                }
            }
            BufferUploader.drawWithShader(vertexBuffer.end());
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
