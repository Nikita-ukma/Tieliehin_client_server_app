package tictak;

public class Worker extends Thread{

	private final int id;
	private final Data data;
	
	public Worker(int id, Data d){
		this.id = id;
		data = d;
		this.start();
	}
	
	@Override
	public synchronized void run(){
		for (int i=0; i<5 ;i++){
			if (id == 1) {
				data.Tic();
			}
			else if(id==2) {
				data.Tak();
			}
			else {
				data.Toe();
			}

			}
		}
	}
