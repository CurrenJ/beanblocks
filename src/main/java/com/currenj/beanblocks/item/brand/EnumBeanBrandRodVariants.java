package com.currenj.beanblocks.item.brand;

import net.minecraft.util.IStringSerializable;

public enum EnumBeanBrandRodVariants implements IStringSerializable
{
    IRON_ROD(0, 0,"brand_rod_iron");

    private static final EnumBeanBrandRodVariants[] META_LOOKUP = new EnumBeanBrandRodVariants[values().length];
    private static final EnumBeanBrandRodVariants[] FILTER_DMG_LOOKUP = new EnumBeanBrandRodVariants[values().length];
    private final int meta;
    private final int brandRodVariantDamage;
    private final String unlocalizedName;

    /**
     * An array containing 3 floats ranging from 0.0 to 1.0: the red, green, and blue components of the corresponding
     * color.
     */

    private EnumBeanBrandRodVariants(int metaIn, int brandRodVariantDamageIn, String unlocalizedNameIn)
    {
        this.meta = metaIn;
        this.brandRodVariantDamage = brandRodVariantDamageIn;
        this.unlocalizedName = unlocalizedNameIn;
    }

    public int getMetadata()
    {
        return this.meta;
    }

    public int getBrandRodVariantDamage()
    {
        return this.brandRodVariantDamage;
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public static EnumBeanBrandRodVariants byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= FILTER_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return FILTER_DMG_LOOKUP[damage];
    }

    public static EnumBeanBrandRodVariants byMetadata(int meta)
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
        for (EnumBeanBrandRodVariants enumbrandrodvariant : values())
        {
            META_LOOKUP[enumbrandrodvariant.getMetadata()] = enumbrandrodvariant;
            FILTER_DMG_LOOKUP[enumbrandrodvariant.getBrandRodVariantDamage()] = enumbrandrodvariant;
        }
    }

    @Override
    public String getName() {
        return this.unlocalizedName;
    }
}
