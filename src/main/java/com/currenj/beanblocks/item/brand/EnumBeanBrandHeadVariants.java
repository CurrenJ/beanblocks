package com.currenj.beanblocks.item.brand;

import net.minecraft.util.IStringSerializable;

public enum EnumBeanBrandHeadVariants implements IStringSerializable
{
    BEAN_FARMER(0, 0,"brand_head_bean_farmer"),
    COMPANION(1, 1,"brand_head_companion");

    private static final EnumBeanBrandHeadVariants[] META_LOOKUP = new EnumBeanBrandHeadVariants[values().length];
    private static final EnumBeanBrandHeadVariants[] FILTER_DMG_LOOKUP = new EnumBeanBrandHeadVariants[values().length];
    private final int meta;
    private final int brandHeadVariantDamage;
    private final String unlocalizedName;

    /**
     * An array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the corresponding
     * color.
     */

    private EnumBeanBrandHeadVariants(int metaIn, int brandHeadVariantDamageIn, String unlocalizedNameIn)
    {
        this.meta = metaIn;
        this.brandHeadVariantDamage = brandHeadVariantDamageIn;
        this.unlocalizedName = unlocalizedNameIn;
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public int getBrandHeadVariantDamage()
    {
        return this.brandHeadVariantDamage;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public static EnumBeanBrandHeadVariants byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= FILTER_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return FILTER_DMG_LOOKUP[damage];
    }

    public static EnumBeanBrandHeadVariants byMetadata(int meta)
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
        for (EnumBeanBrandHeadVariants enumbrandheadvariant : values())
        {
            META_LOOKUP[enumbrandheadvariant.getMetadata()] = enumbrandheadvariant;
            FILTER_DMG_LOOKUP[enumbrandheadvariant.getBrandHeadVariantDamage()] = enumbrandheadvariant;
        }
    }

    @Override
    public String getName() {
        return this.unlocalizedName;
    }
}
