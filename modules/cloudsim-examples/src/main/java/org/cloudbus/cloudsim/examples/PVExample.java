package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.photovoltaics.PVChart;
import org.cloudbus.cloudsim.photovoltaics.PVFarm;
import org.cloudbus.cloudsim.photovoltaics.RandomRunner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class PVExample {
    public static void main(String[] args) {
//        creating photovoltaic farm with parameters: angle, number of panels, latitude, longitude, panel efficiency, peak power
        PVFarm pvFarm = new PVFarm(10.0f, 10, 50.0f, 20.0f,  19.30f, 0.3f);
//        calculating produced energy for date provided as a parameter using pattern: YearMonthDay
        double[] producedPower = pvFarm.calculateSunPower("20150919");
//        double[] producedPower = pvFarm.calculateSunPower("20110914");
        if (producedPower == null) {
            System.out.println("error");
            return;
        }

        boolean enableOutput = true;
        boolean outputToFile = true;
        String inputFolder = "";
        String outputFolder = "data_center_power_consumption";
        String workload = "random"; // Random workload
        String vmAllocationPolicy = "dvfs"; // DVFS policy without VM migrations
        String vmSelectionPolicy = "";
        String parameter = "";

//        RandomRunner simulates energy consumption for power center and writes result to results.json
        new RandomRunner(
                enableOutput,
                outputToFile,
                inputFolder,
                outputFolder,
                workload,
                vmAllocationPolicy,
                vmSelectionPolicy,
                parameter);

//        get energy consumption data from results.json
        double[] consumedPower = getData("results.json");

//        calculating exported and imported energy
        double[] exported = new double[consumedPower.length];
        double[] imported = new double[consumedPower.length];
        for (int i = 0; i < exported.length; i++) {
            double tmp = producedPower[i] - consumedPower[i];
            if (tmp > 0.0) {
                exported[i] = tmp;
                imported[i] = 0;
            }
            else {
                exported[i] = 0;
                imported[i] = Math.abs(tmp);
            }
        }

//        create charts
        EventQueue.invokeLater(() -> {
            PVChart producedPvChart = new PVChart(producedPower, "produced", Color.BLUE);
            producedPvChart.setVisible(true);

            PVChart consumedPvChart = new PVChart(consumedPower, "consumed", Color.RED);
            consumedPvChart.setVisible(true);

            PVChart exportedPvChart = new PVChart(exported, "exported", Color.GREEN);
            exportedPvChart.setVisible(true);

            PVChart importedPvChart = new PVChart(imported, "imported", Color.MAGENTA);
            importedPvChart.setVisible(true);

        });
    }

    private static double[] getData(String fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get("powerConsumption");
            double[] result = new double[jsonArray.size() + 1];
            for (int i = 0; i < jsonArray.size(); i++) {
                result[i] = Double.parseDouble(jsonArray.get(i).toString()) / 3600;
            }
            result[result.length - 1] = 0.0;
            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
