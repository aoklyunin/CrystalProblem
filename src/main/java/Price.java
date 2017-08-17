import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aokly on 17.08.2017.
 */
public class Price {
    public Price(long id, String product_code, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.product_code = product_code;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    private long id; // идентификатор в БД
    private String product_code; // код товара
    private int number; // номер цены
    private int depart; // номер отдела
    private Date begin; // начало действия
    private Date end; // конец действия
    private long value; // значение цены в копейках

    public long getId() {
        return id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getValue() {
        return value;
    }

    // копирование цены с новыми временными границами
    public Price(Price price, Date begin, Date end) {
        this.id = price.id;
        this.product_code = price.product_code;
        this.number = price.number;
        this.depart = price.depart;
        this.value = price.value;
        this.begin = begin;
        this.end = end;
    }



    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd.MM");
        return "{" +
                "id=" + id +
                ", product_code='" + product_code + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + df.format(begin) +
                ", end=" + df.format(end) +
                ", value=" + value +
                '}';
    }
   /* @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd.MM");
        return  "{begin=" + df.format(begin) +
                ", end=" + df.format(end) +
                ", value=" + value +
                '}';
    }*/

    public boolean claimCrossed(Price price) {
        return this.product_code == price.product_code && this.number == price.number;
    }

    Price(Price price) {
        this.id = price.id;
        this.product_code = price.product_code;
        this.number = price.number;
        this.depart = price.depart;
        this.begin = price.begin;
        this.end = price.end;
        this.value = price.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (id != price.id) return false;
        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (value != price.value) return false;
        if (product_code != null ? !product_code.equals(price.product_code) : price.product_code != null) return false;
        if (begin != null ? !begin.equals(price.begin) : price.begin != null) return false;
        return end != null ? end.equals(price.end) : price.end == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (product_code != null ? product_code.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + (begin != null ? begin.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }
}
