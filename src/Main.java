package src;
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        while(true){
            game.run(); 
            try {
                Thread.sleep(20); // Control the frame rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
