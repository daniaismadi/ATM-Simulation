package investments;

import atm.ATM;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class MutualFundsStocks implements Serializable {
    private final ArrayList<Stock> lowRiskStocks = new ArrayList<>();
    private final ArrayList<Stock> mediumRiskStocks = new ArrayList<>();
    private final ArrayList<Stock> highRiskStocks = new ArrayList<>();
    private final ATM atm;
    private final Calendar date;

    public MutualFundsStocks(ATM atm){
        this.atm = atm;
        date = atm.getDate();
        createLowRiskStocks();
        createMediumRiskStocks();
        createHighRiskStocks();
    }

    private void createLowRiskStocks(){
        lowRiskStocks.add(new Stock("MICROSOFT CORP","MSFT",0.0));
        lowRiskStocks.add(new Stock("AMAZON COM INC","AMZN",0.0));
        lowRiskStocks.add(new Stock("APPLE INC","AAPL",0.0));
        lowRiskStocks.add(new Stock("ADOBE INC","ADBE",0.0));
        lowRiskStocks.add(new Stock("FACEBOOK INC","FB",0.0));
        lowRiskStocks.add(new Stock("NETFLIX INC","NFLX",0.0));
        for (Stock stock : lowRiskStocks){
            stock.increaseNumShares(1000);
//            stock.updateStock(date);
        }
    }

    private void createMediumRiskStocks(){
        mediumRiskStocks.add(new Stock("MICROSOFT CORP","MSFT", 0.0));
        mediumRiskStocks.add(new Stock("AMAZON COM INC","AMZN", 0.0));
        mediumRiskStocks.add(new Stock("APPLE INC","AAPL", 0.0));
        mediumRiskStocks.add(new Stock("PAYPAL INC","PYPL",0.0));
        mediumRiskStocks.add(new Stock("Expedia Group, Inc.","EXPE",0.0));
        mediumRiskStocks.add(new Stock("Starbucks","SBUX",0.0));
        for (Stock stock : mediumRiskStocks){
            stock.increaseNumShares(1000);
//            stock.updateStock(date);
        }
    }

    private void createHighRiskStocks(){
        highRiskStocks.add(new Stock("Starbucks","SBUX",0.0));
        highRiskStocks.add(new Stock("Visa Inc","V",0.0));
        highRiskStocks.add(new Stock("BAIDU INC","BIDU",0.0));
        highRiskStocks.add(new Stock("Barrick Gold Corporation","GOLD",0.0));
        highRiskStocks.add(new Stock("22nd Century Group Inc","XXII",0.0));
        highRiskStocks.add(new Stock("Smucker","SJM",0.0));
        for (Stock stock : highRiskStocks){
            stock.increaseNumShares(1000);
//            stock.updateStock(date);
        }
    }

    public ArrayList<Stock> getLowRiskStocks(){return lowRiskStocks;}

    public ArrayList<Stock> getMediumRiskStocks(){return mediumRiskStocks;}

    public ArrayList<Stock> getHighRiskStocks(){return highRiskStocks;}
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        ois.defaultReadObject();
    }

    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("readObjectNoData, this should never happen!");
        System.exit(-1);
    }

}

