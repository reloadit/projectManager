package com.copyrightException.ProjectManager;

public enum SlotColors {
    WHITE("pm-slot-white", "white"),
    GREEN("pm-slot-green", "green"),
    BLUE("pm-slot-blue", "blue"),
    LIGH_BLUE("pm-slot-light-blue", "light-blue"),
    YELLOW("pm-slot-yellow", "yellow"),
    ORANGE("pm-slot-orange", "orange"),
    LIGHT_GREEN("pm-slot-light-green", "light-green"),
    AMBER("pm-slot-amber", "amber"),
    PRUSSIAN_BLUE("pm-slot-prussian-blue", "prussian-blue"),
    AQUA("pm-slot-aqua", "aqua");
    
    private final String styleName;
    private final String colorName;

    private SlotColors(String styleName, String colorName) {
        this.styleName = styleName;
        this.colorName = colorName;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getColorName() {
        return colorName;
    }

}
