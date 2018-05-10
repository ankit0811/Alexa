package com.aka.DollarToRupee;

import java.util.Map;

public class TestMain {

    public static void main(String args[]){
        GetTopExchanges dollarRupee = new GetTopExchanges();
        Map<String, Double> map = dollarRupee.getExchangeRateMap();

        String outputText="" ;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            outputText += entry.getKey() + " with rate of " + entry.getValue() +", ";
        }

        System.out.println(outputText);

    }
}
