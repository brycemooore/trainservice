public class TestDriver {

    public static void main(String[] args){

        Database test = new Database("javatest");
        test.connectToDatabase();
//        test.createTable("passengers");
        System.out.println(test.getTableList());
        test.closeConnection();
    }
}
