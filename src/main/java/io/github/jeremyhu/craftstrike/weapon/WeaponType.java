package io.github.jeremyhu.craftstrike.weapon;

public enum WeaponType {
    AK47(1,50D,10D,30,4,2,50,0.02F),
    SAWN_OFF(2,5D,20D,7,6,20,50,0.1F);


    public double range;
    public double PrimaryDamage;
    public int clip;
    public int clipNum;
    public int CD;
    public int data;
    public int reloadTime;
    public float offset;

    private WeaponType(int data, double range,double PrimaryDamage,int clip,int clipNum,int CD,int reloadTime,float offset){
        this.range = range;
        this.PrimaryDamage = PrimaryDamage;
        this.clip = clip;
        this.clipNum = clipNum;
        this.CD = CD;
        this.data = data;
        this.reloadTime = reloadTime;
        this.offset = offset;
    }
}
