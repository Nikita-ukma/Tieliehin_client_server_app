import models.Message;
import models.Packet;
import models.Processor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.fail;

public class ListByCriteriaTest {
    private static Connection con;

    @BeforeAll
    public static void initialization(){
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:StoreDB");
            PreparedStatement st = con.prepareStatement("create table if not exists 'test' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text);");
        }catch(ClassNotFoundException e){
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
    }

    // IF START FIRSTLY THEN USE THIS:
//    @BeforeAll
//    public static void insertTestData(){
//        try{
//            Processor processor = new Processor();
//            processor.processPacket(new Packet(new Message(1,1, "Молоко,Опис молока,Виробник 1,100,20.50,1")));
//            processor.processPacket(new Packet(new Message(1, 1, "Хліб,Опис хліба,Виробник 2,200,15.75,2")));
//            processor.processPacket(new Packet(new Message(1, 1, "Масло,Опис масла,Виробник 3,150,35.20,3")));
//            processor.processPacket(new Packet(new Message(1, 1, "Сир,Опис сиру,Виробник 4,80,50.00,4")));
//            processor.processPacket(new Packet(new Message(1, 1, "Яблука,Опис яблук,Виробник 5,500,10.00,5")));
//        }catch (SQLException e){
//            System.out.println("Не вірний SQL запит на вставку");
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    @AfterAll
    public static void showAllData(){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM Products");
            Processor processor = new Processor();
            processor.processPacket(new Packet(new Message(6, 1, ":)")));
            while (res.next()) {
                String name = res.getString("name");
                System.out.println (res.getShort("id")+" "+name);
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   @Test
   public void listByCriteriaTest_idGreaterThanZero() {
       try {
           Processor processor = new Processor();
           processor.processPacket(new Packet(new Message(5, 1, "id > 0")));
       } catch (SQLException e) {
           System.out.println("SQL exception thrown: " + e.getMessage());
           fail("SQL exception thrown: " + e.getMessage());
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }

    @Test
    public void listByCriteriaTest_nameLikeProduct() {
        try {
            Processor processor = new Processor();
            processor.processPacket(new Packet(new Message(5, 1, "name = Молоко")));
        } catch (SQLException e) {
            System.out.println("SQL exception thrown: " + e.getMessage());
            fail("SQL exception thrown: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listByCriteriaTest_quantityGreaterThan200() {
        try {
            Processor processor = new Processor();
            processor.processPacket(new Packet(new Message(5, 1, "quantity_in_stock > 200")));
        } catch (SQLException e) {
            System.out.println("SQL exception thrown: " + e.getMessage());
            fail("SQL exception thrown: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listByCriteriaTest_priceLessThan30() {
        try {
            Processor processor = new Processor();
            processor.processPacket(new Packet(new Message(5, 1, "price_per_unit < 30.00")));
        } catch (SQLException e) {
            System.out.println("SQL exception thrown: " + e.getMessage());
            fail("SQL exception thrown: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listByCriteriaTest_descriptionLikeDesc() {
        try {
            Processor processor = new Processor();
            processor.processPacket(new Packet(new Message(5, 1, "description = Description%")));
        } catch (SQLException e) {
            System.out.println("SQL exception thrown: " + e.getMessage());
            fail("SQL exception thrown: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
