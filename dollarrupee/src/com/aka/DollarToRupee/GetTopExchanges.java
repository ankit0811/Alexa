package com.aka.DollarToRupee;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetTopExchanges {

    private final Map<String, Double> exchangeRateMap=new LinkedHashMap<String, Double>();
    private Double mmRate=0d;

    public GetTopExchanges(){
        try {
            setExchangeRateMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setExchangeRateMap() throws IOException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("https://exchangerateiq.com/send-money-to-india/compare-usd-to-inr?country_from=usa&amount=2999&mode=send&sort_by=rate");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        //System.out.println(EntityUtils.getContentMimeType(entity));
        //System.out.println(EntityUtils.getContentCharSet(entity));
        String htmlOuptut = EntityUtils.toString(entity);

        Document document = Jsoup.parse(htmlOuptut);
        String midMarketRate=document.getElementsByClass("sp-current-mm").get(0).childNodes().get(2).toString().trim();
        Elements elements = document.getElementsByClass("ri-result-wrapper");
        Matcher m = Pattern.compile("\\d+\\.\\d+").matcher(midMarketRate);

        while(m.find()){
            mmRate = Double.parseDouble(m.group());
        }
        //System.out.println("MidMarket rate is "+ mmRate);

        String exchangeName;
        String exchangeRate, value;


        for(int i=0; i<elements.size(); i++) {
            if (elements.get(i).getElementsByClass("ei-sponsored").size()>0){
                continue;
            }
            if(elements.get(i).getElementsByClass("ei-exchange-rate").size()>0) {
                exchangeName = elements.get(i).getElementsByClass("ei-exchange-rate").get(0).childNodes().get(1).childNodes().get(0).toString().trim();
                exchangeRate = elements.get(i).getElementsByClass("ei-ee-rate").get(0).childNodes().get(0).childNode(0).toString().trim();
                value=elements.get(i).getElementsByClass("ei-amount-receive").get(0).childNodes().get(0).toString().trim();
                System.out.println(exchangeName + " : " + exchangeRate + " : "+ value);
                exchangeRateMap.put(exchangeName.split(" ")[0], Double.parseDouble(exchangeRate) );
            }
        }

        System.out.println(exchangeRateMap);
    }

    public Map<String, Double> getExchangeRateMap(){
        return exchangeRateMap;
    }

    public Double getMmRate(){
        return mmRate;
    }
}