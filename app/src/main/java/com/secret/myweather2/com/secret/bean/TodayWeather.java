package com.secret.myweather2.com.secret.bean;

/**
 * Created by sekike on 15-3-24.
 */
public class TodayWeather {
    private String city = null;
    private String updatetime = null;
    private String temperature = null;
    private String humidity = null;
    private String pm25 = null;
    private String quality = null;
    private String fengxiang = null;
    private String wind = null;
    private String date = null;
    private String high = null;
    private String low = null;
    private String climate = null;

    public void setCity(String arg){
        city=arg;
    }
    public void setUpdatetime(String arg){
        updatetime=arg;
    }
    public void setTemperature(String arg){
        temperature=arg;
    }
    public void setHumidity(String arg){
        humidity=arg;
    }
    public void setPm25(String arg){
        pm25=arg;
    }
    public void setQuality(String arg){
        quality=arg;
    }
    public void setWind(String arg){
        wind=arg;
    }
    public void setFengxiang(String arg){
        fengxiang=arg;
    }
    public void setDate(String arg){
        date=arg;
    }
    public void setHigh(String arg){
        high=arg;
    }
    public void setLow(String arg){
        low=arg;
    }
    public void setClimate(String arg){
        climate=arg;
    }
    public String getCity(){
        return city;
    }
    public String getDate(){
        return date;
    }
    public String getHigh(){
        return high;
    }
    public String getLow(){
        return low;
    }
    public String getHumidity(){
        return humidity;
    }
    public String getClimate(){
        return climate;
    }
    public String getUpdatetime(){
        return updatetime;
    }
    public String getPm25(){
        return pm25;
    }
    public String getWind(){
        return wind;
    }
    public String getTemperature(){
        return temperature;
    }
    public String getQuality(){
        return quality;
    }
    public String toString(){
        return "TodayWeather{"+
                "city='" + city +'\'' +
                ",wendu='" + temperature +'\'' +
                ",shidu='" + humidity +'\'' +
                ",pm25='" + pm25 +'\'' +
                ",quality='" + quality +'\'' +
                ",fengxiang='" + fengxiang +'\'' +
                ",fengli" + wind +'\'' +
                ",date='" + date +'\'' +
                ",high='" + high +'\'' +
                ",low='" + low +'\'' +
                ",updatetime='" + updatetime +'\'' +
                ",type='" + climate +'\'' +
                '}';
    }
}
