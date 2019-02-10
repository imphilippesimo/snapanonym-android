package com.zerofiltre.snapanonym.data;

public class Snap extends Data {

    private String info;
    private int ImageResource;

    public Snap(int id, String info, int imageResource) {
        super(id);
        this.info = info;
        ImageResource = imageResource;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }
}
