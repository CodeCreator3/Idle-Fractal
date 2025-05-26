package src;

public class Game {
    private Renderer renderer = new Renderer();

    public void init() {
        renderer.init();
        int width = 800, height = 600;
        double startX = width / 2.0;
        double startY = height - 50;
        renderer.render(FractalGenerator.generateFractalTree(
                startX, startY,
                -90, // pointing straight up
                100, // initial branch length
                10, // recursion depth
                25, // angle delta
                0.7 // length scaling
        ));
    }

    public void run() {
        
    }
}