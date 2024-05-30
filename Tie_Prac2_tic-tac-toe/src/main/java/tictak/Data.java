package tictak;

public class Data {
    private int state=1;

    public int getState() { return state; }

   synchronized void Tic(){
        while (getState()!=1) {
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
        while (getState()!=2) {
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
        while (getState()!=3) {
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
