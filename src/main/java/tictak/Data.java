package tictak;

public class Data {
    private int state=1;

    public int getState() { return state; }

   synchronized public void Tic(){
        while (state!=1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.print("Tic-");
        state=2;
        notifyAll();
    }
    synchronized void Tak(){
        while (state!=2) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.print("Tak-");
        state=3;
        notifyAll();
    }
    synchronized void Toe(){
        while (state!=3) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Toy");
        state=1;
        notifyAll();
    }
}
