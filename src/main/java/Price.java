import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by aokly on 17.08.2017.
 */
public class Price {

    Date getBegin() {
        return begin;
    }

    Date getEnd() {
        return end;
    }

    long getValue() {
        return value;
    }

    // копирование цены с новыми временными границами
    Price(Price price, Date begin, Date end) {
        this(price.product_code,price.number,price.depart,begin,end,price.value);
    }

    public String toString(){
        DateFormat df = new SimpleDateFormat("dd.MM");
        return "{"+this.product_code+"|"+this.number+"|"+this.depart+ "[" + df.format(begin) +"-"+ df.format(end)+"] "+this.value+"}";
    }

    // проверяем, стоит ли вообще проверять цены на пересечение
    boolean claimCrossed(Price price) {
        return this.product_code == price.product_code && this.number == price.number&&this.depart==price.depart&&
                !(this.begin.equals(price.begin)&&this.end.equals(price.end));
    }

    void setBegin(Date begin) {
        this.begin = begin;
    }

    String getProduct_code() {
        return product_code;
    }

    int getNumber() {
        return number;
    }

    int getDepart() {
        return depart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (product_code != null ? !product_code.equals(price.product_code) : price.product_code != null) return false;
        if (begin != null ? !begin.equals(price.begin) : price.begin != null) return false;
        return end != null ? end.equals(price.end) : price.end == null;
    }

    private static int uniqueId = 0;

    static Comparator<Price> getComparator(){
        return new Comparator<Price>() {
            @Override
            public int compare(Price o1, Price o2) {
                int dP = o1.getProduct_code().compareTo(o2.getProduct_code());
                if (dP != 0)
                    return dP;
                else {
                    int dD = o1.getDepart() - o2.getDepart();
                    if (dD != 0)
                        return dD;
                    else {
                        int dN = o1.getNumber() - o2.getNumber();
                        if (dN != 0)
                            return dN;
                        else {
                            return o1.getBegin().compareTo(o2.getBegin());
                        }
                    }
                }
            }
        };
    }

    private static int getUniqueId(){
        return uniqueId++;
    }

    private Price(long id, String product_code, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.product_code = product_code;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    Price(String product_code, int number, int depart, Date begin, Date end, long value) {
        this(Price.getUniqueId(),product_code,number,depart,begin,end,value);
    }

    private long id; // идентификатор в БД
    private String product_code; // код товара
    private int number; // номер цены
    private int depart; // номер отдела
    private Date begin; // начало действия
    private Date end; // конец действия
    private long value; // значение цены в копейках

}
