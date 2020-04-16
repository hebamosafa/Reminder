package com.example.lib;

public class U1 extends Rocket {

    U1() {
        super(100, 10, 18);
        this.current_weight=0;



    }

    @Override
    public boolean land() {
        Chance_of_landing_crash= (current_weight / Max_weight);
        if(Chance_of_landing_crash<3.9)
            return true;
        return false;
    }

    @Override
    public boolean launch() {
        Chance_of_launch_explosion= 5 * (current_weight / Max_weight);
        if(Chance_of_launch_explosion<0.8)
            return true;
        return false;

    }
}
