package com.currenj.beanblocks.item.filter.press;

import net.minecraft.util.IStringSerializable;

public enum EnumPressFilterVariants implements IStringSerializable
{
    BRICKMOLD(0, 0,"filter_brick_mold"),
    FUNNEL(1, 1,"filter_funnel");

    private static final EnumPressFilterVariants[] META_LOOKUP = new EnumPressFilterVariants[values().length];
    private static final EnumPressFilterVariants[] FILTER_DMG_LOOKUP = new EnumPressFilterVariants[values().length];
    private final int meta;
    private final int filterVariantDamage;
    private final String unlocalizedName;

    /**
     * An array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the corresponding
     * color.
     */

    private EnumPressFilterVariants(int metaIn, int filterVariantDamageIn, String unlocalizedNameIn)
    {
        this.meta = metaIn;
        this.filterVariantDamage = filterVariantDamageIn;
        this.unlocalizedName = unlocalizedNameIn;
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public int getFilterVariantDamage()
    {
        return this.filterVariantDamage;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public static EnumPressFilterVariants byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= FILTER_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return FILTER_DMG_LOOKUP[damage];
    }

    public static EnumPressFilterVariants byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String toString()
    {
        return this.unlocalizedName;
    }

    static
    {
        for (EnumPressFilterVariants enumfiltervariant : values())
        {
            META_LOOKUP[enumfiltervariant.getMetadata()] = enumfiltervariant;
            FILTER_DMG_LOOKUP[enumfiltervariant.getFilterVariantDamage()] = enumfiltervariant;
        }
    }

    @Override
    public String getName() {
        return this.unlocalizedName;
    }
}
