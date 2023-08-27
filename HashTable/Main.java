package Seminar004.CW.Seminar.HashTable;

public class Main {
    public static void main(String[] args) {

        var employee1 = new Employee("AAAA", 33);
        System.out.println("employee1.hashCode() = " + employee1.hashCode());
        var employee2 = new Employee("AAAA", 41);
        System.out.println("employee2.hashCode() = " + employee2.hashCode());

        HashMap<String, String> hashMap1 = new HashMap<>(4);

        String oldValue;

        oldValue = hashMap1.put("+79007774431", "AAAAAAA");
        oldValue = hashMap1.put("+79007774432", "BBBBB");
        oldValue = hashMap1.put("+79007774431", "CCCCCCC1");
        oldValue = hashMap1.put("+79007774433", "CCCCCCC2");
        oldValue = hashMap1.put("+79007774434", "CCCCCCC3");
        oldValue = hashMap1.put("+79007774435", "CCCCCCC4");
        oldValue = hashMap1.put("+79007774436", "CCCCCCC5");
        oldValue = hashMap1.put("+79007774437", "CCCCCCC6");
        oldValue = hashMap1.put("+79007774438", "CCCCCCC7");
        oldValue = hashMap1.put("+79007774439", "CCCCCCC8");

        for (HashMap.Entity entity : hashMap1) {
            System.out.println("entity = " + entity);
        }

        System.out.println("hashMap1 = " + hashMap1);

    }

}
