

public class TargetClass {

    public void a() {
        System.out.println("called... a");
    }


    public void b() {
        System.out.println("called... b");
    }

    public static void main(String[] args) {
        System.out.println("called.. main");

        TargetClass targetClass = new TargetClass();
        targetClass.a();
        targetClass.b();

    }
}


