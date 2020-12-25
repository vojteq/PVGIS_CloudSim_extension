package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.photovoltaics.PVChart;
import org.cloudbus.cloudsim.photovoltaics.PVFarm;

import java.awt.*;

public class MyExample {
    public static void main(String[] args) {
        PVFarm pvFarm = new PVFarm(10.0f, 10, 50.0f, 20.0f,  19.30f, 0.3f);
        double[] producedPower = pvFarm.calculateSunPower("20101212");
        if (producedPower == null) {
            System.out.println("error");
            return;
        }
        double[] consumedPower = new double[24];

        EventQueue.invokeLater(() -> {
            PVChart pvChart = new PVChart(producedPower, consumedPower);
            pvChart.setVisible(true);
        });
    }
}
