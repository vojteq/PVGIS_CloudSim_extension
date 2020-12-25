package org.cloudbus.cloudsim.photovoltaics;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;
import java.util.Scanner;

public class PVFarm {

    private float angle;
    private int numberOfPanels;
    private float efficiency;
    private float peakPower;
    private float latitude;
    private float longitude;

    public PVFarm(float angle, int numberOfPanels, float latitude, float longitude) {
        this.angle = angle;
        this.numberOfPanels = numberOfPanels;
        this.latitude = latitude;
        this.longitude = longitude;
        this.efficiency = 19.10f;
        this.peakPower = 0.31f;
    }

    public PVFarm(float angle, int numberOfPanels, float latitude, float longitude, float efficiency, float peakPower) {
        this.angle = angle;
        this.numberOfPanels = numberOfPanels;
        this.efficiency = efficiency;
        this.peakPower = peakPower;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double[] calculateSunPower(String date) { // rokMiesiacDzien np 20051103 -> 3 listopada 2005
        JSONArray hoursJsonArray = getData(date);
        if (hoursJsonArray == null)
            return null;

        double[] producedEnergy = new double[24];

        for (int i = 0; i < hoursJsonArray.size(); i++) {
            double tmp = Double.parseDouble (((JSONObject) hoursJsonArray.get(i)).get("P").toString());
            System.out.println(tmp);
            producedEnergy[i] = tmp * numberOfPanels;
        }

        return producedEnergy;
    }

    private JSONArray getData(String date) {
        JSONArray hoursJsonArray = null;
        String httpRequestUrl = "https://re.jrc.ec.europa.eu/api/seriescalc?outputformat=json&pvcalculation=1&" +
                "lat=" + latitude +"&" +
                "lon=" + longitude + "&" +
                "peakpower=" + peakPower + "&" +
                "loss=" + (100.0 - efficiency) + "&" +
                "angle" + angle;
        try {
            URL urlRequest = new URL(httpRequestUrl);
            Scanner scanner = new Scanner(urlRequest.openStream());
            String response = scanner.useDelimiter("\\Z").next();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

            JSONObject outputJsonObject = (JSONObject) jsonObject.get("outputs");
            JSONArray array = (JSONArray) outputJsonObject.get("hourly");
            hoursJsonArray = new JSONArray();
            for (int i = 0; i < array.size(); i++) {
                JSONObject tmpJsonObject = (JSONObject) array.get(i);
                String[] time = tmpJsonObject.get("time").toString().split(":");
                if (date.equals(time[0])) {
                    hoursJsonArray.add(tmpJsonObject);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return hoursJsonArray;
        }
    }
}
