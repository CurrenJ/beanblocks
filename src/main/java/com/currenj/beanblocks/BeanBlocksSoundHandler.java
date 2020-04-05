package com.currenj.beanblocks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BeanBlocksSoundHandler {

    public static SoundEvent HARRY_NOISE;

    public static void registerSounds(){
        HARRY_NOISE = registerSound("block.noise_machine");
    }

    private static SoundEvent registerSound(String name){
        ResourceLocation location = new ResourceLocation(BeanBlocks.modId, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
