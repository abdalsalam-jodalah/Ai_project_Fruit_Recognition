package application.ai.BackEnd;

public class Fruit {
    public String sweetness;
    public String color;
    public String name;
    public String ActualName;
    public Fruit(){
       this.sweetness = null;
       this.color = null;
       this.name = null;
       this.ActualName=null;
    }
    public Fruit(String name,String sweetness, String color){
        this.sweetness = sweetness;
        this.color = color;
        this.name = name;
        this.ActualName=null;
    }

    public Fruit(String name,String sweetness, String color, String ActualName){
        this.sweetness = sweetness;
        this.color = color;
        this.name = name;
        this.ActualName=ActualName;
    }

    public void setSweetness(String sweetness) {
        this.sweetness = sweetness;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActualName(String actualName) {
        ActualName = actualName;
    }


    public String getSweetness() {
        return sweetness;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getActualName() {
        return ActualName;
    }
}
