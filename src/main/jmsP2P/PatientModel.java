package main.jmsP2P;

import java.io.Serializable;

public class PatientModel implements Serializable {

    private int id;
    private String name;
    private String insuranceProvider;
    private Double coPay;
    private Double amountTobePaid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public Double getCoPay() {
        return coPay;
    }

    public void setCoPay(Double coPay) {
        this.coPay = coPay;
    }

    public Double getAmountTobePaid() {
        return amountTobePaid;
    }

    public void setAmountTobePaid(Double amountTobePaid) {
        this.amountTobePaid = amountTobePaid;
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", coPay=" + coPay +
                ", amountTobePaid=" + amountTobePaid +
                '}';
    }
}
