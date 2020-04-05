package com.currenj.beanblocks.item.filter.recycler;

import net.minecraft.util.IStringSerializable;

public enum EnumRecyclerFilterVariants implements IStringSerializable
{
    METALFILTER(0, 0,"metal_filter"),
    GEMFILTER(1, 1,"gem_filter"),
    WOODENFILTER(2, 2,"wooden_filter"),
    ORGANICFILTER(3, 3,"organic_filter");

    private static final EnumRecyclerFilterVariants[] META_LOOKUP = new EnumRecyclerFilterVariants[values().length];
    private static final EnumRecyclerFilterVariants[] FILTER_DMG_LOOKUP = new EnumRecyclerFilterVariants[values().length];
    private final int meta;
    private final int filterVariantDamage;
    private final String unlocalizedName;

    /**
     * An array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the corresponding
     * color.
     */

    private EnumRecyclerFilterVariants(int metaIn, int filterVariantDamageIn, String unlocalizedNameIn)
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

    public static EnumRecyclerFilterVariants byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= FILTER_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return FILTER_DMG_LOOKUP[damage];
    }

    public static EnumRecyclerFilterVariants byMetadata(int meta)
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
        for (EnumRecyclerFilterVariants enumfiltervariant : values())
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
