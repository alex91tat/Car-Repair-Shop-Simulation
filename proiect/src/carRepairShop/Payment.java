package carRepairShop;

import java.sql.Date;

public class Payment {
    private int carId;
    private float amount;
    private Date paymentDate;

    public Payment(int carId, float amount, Date paymentDate) {
        this.carId = carId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public int getCarId() {
        return carId;
    }

    public float getAmount() {
        return amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }
}
