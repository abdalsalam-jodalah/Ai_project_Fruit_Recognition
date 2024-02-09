package application.ai;

public  class fruits {
    public  String color;
    public    Integer sweetnes;

    public  String desiredName;
    public   String actualName;

    public  fruits() {
    }
    public  fruits(String color, Integer sweetnes, String desiredName, String actualName) {
        this.color = color;
        this.sweetnes = sweetnes;
        this.desiredName = desiredName;
        this.actualName = actualName;
    }

    public  fruits(String color, int sweetnes, String desiredName, String actualName) {
        this.color = color;
        this.sweetnes = sweetnes;
        this.desiredName = desiredName;
        this.actualName = actualName;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSweetnes(Integer sweetnes) {
        this.sweetnes = sweetnes;
    }

    public void setDesiredName(String desiredName) {
        this.desiredName = desiredName;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getColor() {
        return color;
    }

    public Integer getSweetnes() {
        return sweetnes;
    }

    public String getDesiredName() {
        return desiredName;
    }

    public String getActualName() {
        return actualName;
    }
}
