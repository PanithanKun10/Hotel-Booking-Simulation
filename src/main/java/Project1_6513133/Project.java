//1.Panithan Kunsuntrontham 6513133 2. Thunyaphat Permsup 6513167 3.Mattana Olarikded 6513173 4.Suphanai chalood 6513176 
package Project1_6513133;

import java.util.*;
import java.io.*;

class InvaildInputException extends Exception{
    public InvaildInputException(){};
    public InvaildInputException(String m){super(m);};
}
class MissingInputException extends ArrayIndexOutOfBoundsException{
    public MissingInputException(){};
    public MissingInputException(String m){super(m);};
}

class Input{
    private String path,filename;
    private Scanner scan;
    public Input(String p,String n){
        path = p;
        filename = n;
        scan = new Scanner(System.in);
    }
    public void New_try(ArrayList<Hotel> h){
        boolean suc = false;
        while(!suc){
            try(Scanner filescan = new Scanner(new File(path+filename))){
                suc = true;
                System.out.printf("read hotel data from file %s\n",path+filename);
                while(filescan.hasNext()){
                    P_line(filescan.nextLine(),h);
                }
            }catch(FileNotFoundException e){
                System.out.println(e);
                System.out.println("Enter file name for hotel data = ");
                filename = scan.next();
            }
        }
    
    }
     public void New_try2(ArrayList<Bookings> b,ArrayList<Hotel> h){
        boolean suc = false;
        while(!suc){
            try(Scanner filescan = new Scanner(new File(path+filename))){
                suc = true;
                System.out.printf("\nread booking data from file %s\n",path+filename);
                int l=1;
                while(filescan.hasNext()){
                    P_line2(filescan.nextLine(),b,h,l);
                    l++;
                }
               
            }catch(FileNotFoundException e){
                System.out.println(e);
                System.out.println("Enter file name for booking data = ");
                filename = scan.next();
            }
        }
    
    }
    public void P_line(String line,ArrayList<Hotel> h){
        String []col=line.split(",");
        String type = col[0].trim();
        String name = col[1].trim();
        double rate = Integer.parseInt(col[2].trim());
        if(type.compareToIgnoreCase("r")==0){
            Room r = new Room(type,name,rate);
            h.add(r);
        }else if(type.compareToIgnoreCase("m")==0){
            Meal M = new Meal(type,name,rate);
            h.add(M);
        }
    }
    public void P_line2(String line,ArrayList<Bookings> b,ArrayList<Hotel> h,int L){
       String originalLine = line;
       int ErrorFlag=0;
       String []col =line.split(",");
       int ID = Integer.parseInt(col[0].trim());
       String name = col[1].trim();
       int nights;
       try{
           nights = Integer.parseInt(col[2].trim());
           if(nights<0) throw new InvaildInputException("");
       }
       catch(ArrayIndexOutOfBoundsException | NumberFormatException | InvaildInputException e){
            System.out.printf("[Line: %d, Column: 2]\t",L);
            if(e.getClass().getName().equals("java.lang.NumberFormatException")){
                System.out.printf("%s",e);
                //System.out.print("Invalid input: incorrect type, changing to default value.");
            }
            else if(e.getClass().getName().equals("java.lang.ArrayIndexOutOfBoundsException")){
                System.out.printf("%s",e);
                //System.out.print("Invalid input: missing input, changing to default value.");
            }
            else{
                System.out.printf("%s For input: %s", e, col[2].trim()); 
            }
            System.out.print("\n");
            nights=0;
            ErrorFlag=1;
       }
       int []items = new int [h.size()];
           for(int i=0; i<h.size(); i++){
               try{
           
                    items[i]=Integer.parseInt(col[i+3].trim());
                    if(items[i]<0) throw new InvaildInputException("");
               }
               catch(ArrayIndexOutOfBoundsException | NumberFormatException | InvaildInputException e){
                   System.out.printf("[Line: %d, Column: %d]\t",L,i+3);
                   if(e.getClass().getName().equals("java.lang.NumberFormatException")){
                       System.out.printf("%s",e);
                       //System.out.printf("%s Invalid input: incorrect type, changing to default value.",e);
                   }
                   else if(e.getClass().getName().equals("java.lang.ArrayIndexOutOfBoundsException")){
                       System.out.printf("%s %d columns missing","Project1_6513133.MissingInputException:",items.length-(col.length-3));
                       //System.out.printf("%s Invalid input: missing input, changing to default value.",e);
                   }
                   else{
                       System.out.printf("%s For input: %s", e, col[i+3].trim()); 
                   }
                   System.out.print("\n");
                   items[i]=0;
                   ErrorFlag=1;
               }
          }
        if(ErrorFlag==1){
            System.out.printf("Original [%s] --> Correction ", originalLine);
            System.out.printf("[%d,%7s,%3d",ID,name,nights);
            for(int i=0; i<h.size(); i++){
                System.out.printf(",%3d",items[i]);
            }
            System.out.print("]\n\n");
        }
        Customer C = new Customer(ID,name,nights,items);
        if(C.Check_exists(b)&&b.size()>0){
          C.set_item_name(h);
          C.Process(h);
          C.set_cashback(C.same_person(b));
          C.set_redemption();
          b.add(C);
          for(int i=0; i<h.size(); i++){
            h.get(i).set_total_unit(items[i],nights);
       
          }
        }else{
        C.set_item_name(h);
        C.Process(h);
        b.add(C);
        for(int i=0; i<h.size(); i++){
            h.get(i).set_total_unit(items[i],nights);
           
          }
        }
    }
}
class Hotel implements Comparable<Hotel>{
private String item_type;
private String item_name;
private double rate,rate_plus;
private int total_sale_unit;
private double total_sale_price=0.00;
public Hotel(String t,String n, double r){
    item_type = t;
    item_name =n;
    rate =r;
}
public int compareTo(Hotel other) {
    if (this.total_sale_unit < other.total_sale_unit)       return 1;	
	else if (this.total_sale_unit> other.total_sale_unit)  return -1;	
	else                        return 0;
}
public void print(){
    
}
public String get_item_name(){
    return this.item_name;
}
public double get_rate(){
    return this.rate;
}

public void set_rate_plus(double r){
        rate_plus = r;
    }
public double get_rate_plus(){
     return  this.rate_plus;
}
public void set_total_unit(int n,int ni){
     this.total_sale_unit+=(n*ni);
     this.set_total_price(this.total_sale_unit);
}
public int get_unit(){
       return this.total_sale_unit;
   }
public void set_total_price(int n){
    this.total_sale_price = n*this.rate_plus;
}
public double get_total_price(){
    return this.total_sale_price;
}
public String get_type(){
 return this.item_type;
}
}



class Room extends Hotel{
    public Room(String t,String n, double r){
        super(t,n,r);
        process();
    }
    public void process(){
        double service_charge =(double)super.get_rate()*0.1;
        double VAT = (double)(super.get_rate()+super.get_rate()*0.1)*0.07;
       super.set_rate_plus(super.get_rate()+service_charge+VAT);
    }
    @Override   
    public void print(){
        System.out.printf("%-20s %-6s %,9.2f %14s %,9.2f\n",super.get_item_name(),"rate =",super.get_rate(),"rate++ =",super.get_rate_plus());
}
}
class Meal extends Hotel{
    private double rate_plus;
    public Meal(String t,String n, double r){
        super(t,n,r);
        process();
    }
     public void set_rate_plus(double r){
         super.set_rate_plus(r);
        rate_plus = r;
    }
     public void process(){
          set_rate_plus(super.get_rate());
     }
    @Override 
    public void print(){
    System.out.printf("%-20s %-6s %,9.2f %14s %,9.2f\n",super.get_item_name(),"rate =",super.get_rate(),"rate++ =",this.rate_plus);
}
}

class Bookings{
    private int ID,nights;
    private int []items;
    private String[]items_name;
    private String name;
    private int next_cash_back;
    private double []Room_price;
    private double Meal_price,bill;
   public Bookings(int i,int n,int [] it,String na){
       ID = i;
       nights = n;
       name = na;
       items= new int[it.length];
       for(i=0;i<it.length; i++){
           items[i]=it[i];
       }
            
   }
   
   public void summary_print(ArrayList<Hotel> h,ArrayList<Bookings> b){
        Collections.sort(h);
       for(int i=0; i<h.size(); i++){
           if(i==0){
                System.out.printf("%s Room Summary %s\n","=".repeat(5),"=".repeat(5)); 
           }
         if(h.get(i).get_type().compareToIgnoreCase("r")==0){       
               System.out.printf("%-20s %s %6d %s %,16.2f %s\n",h.get(i).get_item_name(),"total sales =",h.get(i).get_unit(),"rooms",h.get(i).get_total_price(),"baht");
         }
       }
       for(int i=0; i<h.size(); i++){
           if(i==0){
                System.out.printf("\n%s Meal Summary %s\n","=".repeat(5),"=".repeat(5));
            }
           if(h.get(i).get_type().compareToIgnoreCase("m")==0){ 
                System.out.printf("%-20s %s %6d %s %,16.2f %s\n",h.get(i).get_item_name(),"total sales =",h.get(i).get_unit(),"rooms",h.get(i).get_total_price(),"baht");
           }
        } 
   }
   public void print(){
    }
   public String get_i_name(int n){
        return this.items_name[n];
   }
   
   public int get_n_bookings(int n){
       return this.items[n];
   }
   public int get_ID(){
        return this.ID;
   }
   public void set_item_name(ArrayList<Hotel> h){
       items_name = new String[h.size()];
        for(int i=0; i<h.size(); i++){
            items_name[i]=h.get(i).get_item_name();
        }
   }
   public int get_nights(){
       return this.nights;
   }
   public String get_name(){
       return this.name;
   }
   public void set_room_price(double[] f){
       this.Room_price = new double [f.length];
       for(int i=0; i<this.Room_price.length; i++){
           this.Room_price[i] = f[i];
       }
   }
   public void set_meal_price(double n){
       this.Meal_price=n;
   }
   public double get_total_room_price(){
       double sum =0;
       for(int i=0; i<this.Room_price.length; i++){
           sum+=Room_price[i];
       }
       
       return sum;
   }
   public double get_total_meal_price(){
       double sum=0;
       sum+=(double)(items[items.length-1]*this.Meal_price*this.nights);
       
       return sum;
   }
   
   public void Process(ArrayList<Hotel> h){
       double []price = new double[h.size()-1];
       for(int i=0; i<h.size()-1; i++){
           price[i]=(this.get_nights()*items[i]*h.get(i).get_rate_plus());
       }
       set_room_price(price);
       set_meal_price(h.get(h.size()-1).get_rate_plus());
       set_bill(this.get_total_room_price(),this.get_total_meal_price());
   }
   public void set_bill(double r,double m){
       this.bill=r+m;
   }
   public double get_bill(){
        return this.bill;   
   }
   public double get_meal_price(){
       return this.Meal_price;
   }
   public void Set_nextcash(int n){
       this.next_cash_back=n;
   }
    public int get_nextcashback(){
        return this.next_cash_back;
    }
}
class Customer extends Bookings{
    private int cash_back=0;
    private int next_cash_back=0;
    private int redemption=0;
    private String name;
    public Customer(int i,String na,int n,int [] it){
        super(i,n,it,na);
        name= na;
    }
    @Override
    public void print(){
        int n=0;
     System.out.printf("%-7s %3d, %8s %3d %-10s >> %-1s ( %d) %13s ( %d) %16s ( %d) %20s ( %d) %12s ( %d) \n",
 "Booking",super.get_ID(),this.name+",",super.get_nights(),"nights",super.get_i_name(n),super.get_n_bookings(n),super.get_i_name(n+1),super.get_n_bookings(n+1)
     ,super.get_i_name(n+2),super.get_n_bookings(n+2),super.get_i_name(n+3),super.get_n_bookings(n+3),super.get_i_name(n+4),super.get_n_bookings(n+4));
     
     System.out.printf("%-14s %-10d  %23s %4s %,10.2f %33s\n  %54s %6s %,10.2f\n  %48s %12s %,10.2f %12s = %,d\n  %48s %12s %,10.2f %31s = %,d\n\n","Avaliable cashback =  ",this.cash_back,">> Total room price++"
     ,"=",super.get_total_room_price(),"with service charge and VAT",">> Total meal price","=",super.get_total_meal_price(),">> Total bill","=",super.get_bill(),"redeem",this.redemption,">> Final bill","=",super.get_bill()-this.redemption,
     "cashback for next booking",this.next_cash_back);

    }
    @Override
    public void set_item_name(ArrayList<Hotel> h){
         super.set_item_name(h);     
       }
    @Override
    public void Process(ArrayList<Hotel> h){
         super.Process(h);
         this.set_nextcashback();
         this.set_redemption();
       
    }
    public String Get_name(){
        return this.name;
    }
    public void set_redemption(){
        if(this.cash_back<(0.5*this.get_bill())){
            this.redemption=this.cash_back;
        }else if (this.cash_back>=(0.5*this.get_bill())){
            this.redemption=(int)this.get_bill()/2;
            this.set_nextcashback();
        }
        
    }
    public boolean Check_exists(ArrayList<Bookings> b){
        boolean check = false;
        if(b.size()>0){
            for(int i=b.size()-1; i>=0; i--){
            if(this.name.compareToIgnoreCase(b.get(i).get_name())==0){
                check =true;
                break;
                  }
            }
         }
        return check;
    }
    public void set_cashback(int n){
        this.cash_back += n;
    }
    public int same_person(ArrayList<Bookings> b){
        int sum =0;
        if(b.size()>0){
           for(int i=b.size()-1; i>=0; i--){
            if(this.name.compareToIgnoreCase(b.get(i).get_name())==0){
                sum+=b.get(i).get_nextcashback();
                break;
            } 
        }
      }
        return sum;
    }
    @Override
    public int get_nextcashback(){
        return this.next_cash_back;
    }
    public void set_nextcashback(){
         if(this.cash_back<(0.5*this.get_bill())){
           
         this.next_cash_back=(int)(0.05*super.get_total_room_price());
         super.Set_nextcash(this.cash_back);
        }else if (this.cash_back>=(0.5*this.get_bill())){
           double temp;
           temp=this.cash_back-this.redemption;
           this.next_cash_back=(int)(0.05*super.get_total_room_price()+temp);
          super.Set_nextcash(this.cash_back);
        }
        
       
    }
   }

public class Project {
    public static void main(String args[]){
        String path = "src/main/java/Project1_6513133/";
        //String [] input = {"hotel.txt","bookings.txt","bookings_errors.txt"};
        String [] input = {"hotels.txt","bookings_error.txt","booking.txt"};
        ArrayList<Hotel> H = new ArrayList<Hotel>();
        ArrayList<Bookings> B = new ArrayList<Bookings>();
        Input cal1 = new Input(path,input[0]);
        Input cal2 = new Input(path,input[1]);
        try{
            cal1.New_try(H);
            for(int i=0; i<H.size(); i++){
                H.get(i).print();
            }
            cal2.New_try2(B,H);
            for(int i=0; i<B.size(); i++){
                if(i==0){
                 System.out.printf("%s Booking Processing %s\n","=".repeat(5),"=".repeat(5));
                }
                B.get(i).print();
                if(i==B.size()-1){
                    B.get(i).summary_print(H, B);
                }
            }
        }catch(Exception e){
            
        } 
    }
}
